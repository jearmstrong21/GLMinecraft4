package p0nki.glmc4.client.assets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import p0nki.glmc4.utils.Identifier;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TextureAssembler {

    private static final Map<Identifier, TextureAssembler> assemblers = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger();

    private final Map<Identifier, File> identifiers;
    private final Map<Identifier, Image> images;
    private final int width = 1024;
    private final int height = 512;
    private int pixelCount = 0;

    private TextureAssembler(Identifier identifier) {
        try {
            long start = System.currentTimeMillis();
            identifiers = listFiles(new Identifier(identifier.getNamespace(), ""), Path.of(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(identifier.getPath())).toURI()));
            LOGGER.trace("{} identifiers found", identifiers.size());
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
            Path path = Paths.get("run", "atlas");
            //noinspection ResultOfMethodCallIgnored
            path.toFile().mkdirs();
            try {
                ImageIO.write(image, "png", Paths.get(path.toString(), identifier.getPath() + ".png").toFile());
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            long end = System.currentTimeMillis();
            PrintWriter writer;
            try {
                writer = new PrintWriter(Paths.get(path.toString(), identifier.getPath() + ".txt").toFile());
            } catch (FileNotFoundException e) {
                throw new AssertionError(e);
            }
            writer.println(String.format("IMAGES WRITTEN: %s", identifiers.size()));
            writer.println(String.format("DIMENSIONS: %sx%s", width, height));
            writer.println(String.format("PIXELS USED: %s/%s", pixelCount, width * height));
            writer.println(String.format("TIME: %sms", end - start));
            writer.println("-------------");
            images.forEach((key, value) -> writer.println(String.format("%-64s %-9s %s", key, value.w + "x" + value.h, value.path)));
            writer.close();
            LOGGER.info(identifier, "Created texture atlas of size {}x{} in {}ms", width, height, end - start);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<Identifier, File> listFiles(Identifier identifier, Path path) {
        File file = path.toFile();
        String[] sub = file.list();
        if (sub == null) return new HashMap<>();
        Map<Identifier, File> map = new HashMap<>();
        for (String s : sub) {
            Path subPath = Paths.get(path.toString(), s);
            String identifierString = s;
            if (identifierString.indexOf(".") > 0) {
                identifierString = identifierString.substring(0, identifierString.indexOf("."));
            }
            Identifier nextIdentifier = new Identifier(identifier.getNamespace(), identifier.getPath() + "_" + identifierString);
            if (identifier.getPath().equals("")) {
                nextIdentifier = new Identifier(identifier.getNamespace(), identifierString);
            }
            if (subPath.toFile().isFile()) {
                map.put(nextIdentifier, subPath.toFile());
            } else {
                map.putAll(listFiles(nextIdentifier, subPath));
            }
        }
        return map;
    }

    public static TextureAssembler get(Identifier identifier) {
        if (!assemblers.containsKey(identifier))
            assemblers.put(identifier, new TextureAssembler(identifier));
        return assemblers.get(identifier);
    }

    private Image read(Identifier identifier) {
        BufferedImage img;
        try {
            img = ImageIO.read(identifiers.get(identifier));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
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

    public AtlasPosition getTexture(Identifier identifier) {
        Image img = images.get(identifier);
        if (img == null) {
            LOGGER.fatal("Invalid identifier {} for atlas", identifier);
            LOGGER.fatal("Current identifiers: {}", identifiers.keySet().stream().map(Identifier::toString).collect(Collectors.joining(", ")));
            throw new AssertionError();
        }
        return new AtlasPosition(img.x, img.y, img.w, img.h, width, height);
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

}
