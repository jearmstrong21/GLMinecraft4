package p0nki.glmc4.world.gen.layer;

import p0nki.glmc4.world.gen.RandomContext;

public enum ScaleLayer implements ParentedLayer {
    NORMAL {
        @Override
        protected int sample(RandomContext context, int a, int b, int c, int d) {
            if (same(a, b, c, d)) return a;
            if (same(a, b, c)) return a;
            if (same(a, b, d)) return a;
            if (same(a, c, d)) return a;
            if (same(b, c, d)) return b;
            if (same(a, b)) return a;
            if (same(a, c)) return a;
            if (same(a, d)) return a;
            if (same(b, c)) return b;
            if (same(b, d)) return b;
            if (same(c, d)) return c;
            return a;
        }
    },
    FUZZY {
        @Override
        protected int sample(RandomContext context, int a, int b, int c, int d) {
            int i = context.nextInt(4);
            switch (i) {
                case 0:
                    return a;
                case 1:
                    return b;
                case 2:
                    return c;
                case 3:
                    return d;
            }
            return -1;
        }
    };

    private static boolean same(int... x) {
        for (int i = 1; i < x.length; i++) {
            if (x[0] != x[i]) return false;
        }
        return true;
    }

    protected int sample(RandomContext context, int a, int b, int c, int d) {
        return -1;
    }

    @Override
    public int sample(RandomContext context, LayerSampler parent, int x, int z) {
        int center = parent.sample(x >> 1, z >> 1);
        context.initSeed(x >> 1 << 1, z >> 1 << 1);
        int ox = x & 1;
        int oz = z & 1;
        if (ox == 0 && oz == 0) return center;
        else {
            int zpl = parent.sample(x >> 1, (z >> 1) + 1);
            int zpl_rand = context.nextInt(2) == 0 ? center : zpl;
            if (ox == 0) return zpl_rand;
            else {
                int xpl = parent.sample((x >> 1) + 1, z >> 1);
                int xpl_rand = context.nextInt(2) == 0 ? center : xpl;
                if (oz == 0) return xpl_rand;
                else {
                    return sample(context, center, xpl, zpl, parent.sample((x >> 1) + 1, (z >> 1) + 1));
                }
            }
        }
    }
}
