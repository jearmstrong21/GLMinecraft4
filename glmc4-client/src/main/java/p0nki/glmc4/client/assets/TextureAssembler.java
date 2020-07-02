package p0nki.glmc4.client.assets;

import p0nki.glmc4.utils.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class TextureAssembler {

    private static Map<Identifier, File> listFiles(Identifier identifier, String directory) {
        File file = new File(directory);
        String[] sub = file.list();
        if (sub == null) return new HashMap<>();
        Map<Identifier, File> map = new HashMap<>();
        for (String s : sub) {
            File f = new File(directory + "/" + s);
            String identifierString = s;
            if (identifierString.indexOf(".") > 0) {
                identifierString = identifierString.substring(0, identifierString.indexOf("."));
            }
            Identifier nextIdentifier = new Identifier(identifier.getNamespace(), identifier.getPath() + "_" + identifierString);
            if (identifier.getPath().equals(""))
                nextIdentifier = new Identifier(identifier.getNamespace(), identifierString);
            if (f.isFile()) map.put(nextIdentifier, f);
            else map.putAll(listFiles(nextIdentifier, directory + "/" + s));
        }
        return map;
    }

    private static class Image {
        int x;
        int y;
        int w;
        int h;
        int[][] data;
        String path;

        void initData() {
            data = new int[w][h];
        }

        int area() {
            return w * h;
        }
    }

    private final Map<Identifier, File> identifiers;
    private final Map<Identifier, Image> images;

    private Image read(Identifier identifier) throws IOException {
        BufferedImage img = ImageIO.read(identifiers.get(identifier));
        Image image = new Image();
        image.w = img.getWidth();
        image.h = img.getHeight();
        image.initData();
        image.path = identifiers.get(identifier).getAbsolutePath();
        for (int x = 0; x < image.w; x++) {
            for (int y = 0; y < image.h; y++) {
                image.data[x][y] = img.getRGB(x, y);
            }
        }
        return image;
    }

    private final int width = 128;
    private final int height = 64;
    private int pixelCount = 0;

    private boolean canPlace(int[][] data, Identifier identifier, int x, int y) {
        int w = images.get(identifier).w;
        int h = images.get(identifier).h;
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                if (i < 0 || j < 0 || i >= width || j >= height) return false;
                if (data[i][j] != -1) return false;
            }
        }
        return true;
    }

    private void place(int[][] data, Identifier identifier, int x, int y) {
        int w = images.get(identifier).w;
        int h = images.get(identifier).h;
        for (int i = x; i < x + w; i++) {
            for (int j = y; j < y + h; j++) {
                data[i][j] = images.get(identifier).data[i - x][j - y];
            }
        }
        pixelCount += images.get(identifier).area();
        images.get(identifier).x = x;
        images.get(identifier).y = y;
    }

    private void place(int[][] data, Identifier identifier) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (canPlace(data, identifier, x, y)) {
                    place(data, identifier, x, y);
                    return;
                }
            }
        }
        throw new AssertionError(identifier.toString());
    }

    private final String directory;

    private TextureAssembler(Identifier identifier, String directory) throws IOException {
        long start = System.currentTimeMillis();
        this.directory = directory;
        identifiers = listFiles(new Identifier(identifier.getNamespace(), ""), new ResourceLocation(directory).asFile().getAbsolutePath());
        images = new HashMap<>();
        for (Identifier s : identifiers.keySet()) {
            images.put(s, read(s));
        }
        int[][] data = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                data[x][y] = -1;
            }
        }
        identifiers.keySet().stream().sorted((o1, o2) -> {
            int i = Integer.compare(images.get(o2).area(), images.get(o1).area());
            if (i == 0) return o1.toString().compareTo(o2.toString());
            else return i;
        }).forEach(key -> place(data, key));
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, data[x][y]);
            }
        }
        new LocalLocation("atlas/").asFile().mkdirs();
        ImageIO.write(image, "png", new LocalLocation("atlas/" + directory + ".png").asFile());
        long end = System.currentTimeMillis();
        PrintWriter writer = new PrintWriter(new LocalLocation("atlas/" + directory + ".txt").asFile());
        writer.println(String.format("IMAGES WRITTEN: %s", identifiers.size()));
        writer.println(String.format("DIMENSIONS: %sx%s", width, height));
        writer.println(String.format("PIXELS USED: %s/%s", pixelCount, width * height));
        writer.println(String.format("TIME: %sms", end - start));
        writer.println("-------------");
        images.forEach((key, value) -> writer.println(String.format("%-64s %-9s %s", key, value.w + "x" + value.h, value.path)));
        writer.close();
    }

    private static final Map<Identifier, TextureAssembler> assemblers = new HashMap<>();

    public static TextureAssembler get(Identifier identifier) {
        return assemblers.get(identifier);
    }

    public static TextureAssembler assemble(Identifier identifier, String directory) throws IOException {
        return assemblers.put(identifier, new TextureAssembler(identifier, directory));
    }

}
