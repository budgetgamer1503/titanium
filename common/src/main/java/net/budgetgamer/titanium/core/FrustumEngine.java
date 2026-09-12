package net.budgetgamer.titanium.core;

public final class FrustumEngine {

    private final float[][] planes = new float[6][4];
    private double cameraX;
    private double cameraY;
    private double cameraZ;

    public FrustumEngine() {
        for (int i = 0; i < 6; i++) {
            planes[i] = new float[4];
        }
    }

    public void setCameraPosition(double x, double y, double z) {
        this.cameraX = x;
        this.cameraY = y;
        this.cameraZ = z;
    }

    public double getCameraX() {
        return cameraX;
    }

    public double getCameraY() {
        return cameraY;
    }

    public double getCameraZ() {
        return cameraZ;
    }

    public void updateFromMatrix(float[] m) {
        if (m == null || m.length < 16) {
            return;
        }

        planes[0][0] = m[3] + m[0];
        planes[0][1] = m[7] + m[4];
        planes[0][2] = m[11] + m[8];
        planes[0][3] = m[15] + m[12];

        planes[1][0] = m[3] - m[0];
        planes[1][1] = m[7] - m[4];
        planes[1][2] = m[11] - m[8];
        planes[1][3] = m[15] - m[12];

        planes[2][0] = m[3] + m[1];
        planes[2][1] = m[7] + m[5];
        planes[2][2] = m[11] + m[9];
        planes[2][3] = m[15] + m[13];

        planes[3][0] = m[3] - m[1];
        planes[3][1] = m[7] - m[5];
        planes[3][2] = m[11] - m[9];
        planes[3][3] = m[15] - m[13];

        planes[4][0] = m[3] + m[2];
        planes[4][1] = m[7] + m[6];
        planes[4][2] = m[11] + m[10];
        planes[4][3] = m[15] + m[14];

        planes[5][0] = m[3] - m[2];
        planes[5][1] = m[7] - m[6];
        planes[5][2] = m[11] - m[10];
        planes[5][3] = m[15] - m[14];

        for (int i = 0; i < 6; i++) {
            float length = (float) Math.sqrt(
                planes[i][0] * planes[i][0] +
                planes[i][1] * planes[i][1] +
                planes[i][2] * planes[i][2]
            );
            if (length > 1e-6f) {
                float invLength = 1.0f / length;
                planes[i][0] *= invLength;
                planes[i][1] *= invLength;
                planes[i][2] *= invLength;
                planes[i][3] *= invLength;
            }
        }
    }

    public boolean isBoxVisible(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        for (int i = 0; i < 6; i++) {
            final float a = planes[i][0];
            final float b = planes[i][1];
            final float c = planes[i][2];
            final float d = planes[i][3];

            final double px = (a > 0.0f) ? maxX : minX;
            final double py = (b > 0.0f) ? maxY : minY;
            final double pz = (c > 0.0f) ? maxZ : minZ;

            if ((a * px + b * py + c * pz + d) < 0.0) {
                return false;
            }
        }
        return true;
    }

    public boolean isSphereVisible(double x, double y, double z, double radius) {
        for (int i = 0; i < 6; i++) {
            final float a = planes[i][0];
            final float b = planes[i][1];
            final float c = planes[i][2];
            final float d = planes[i][3];

            if ((a * x + b * y + c * z + d) < -radius) {
                return false;
            }
        }
        return true;
    }

    public double distanceSqToCamera(double x, double y, double z) {
        double dx = x - cameraX;
        double dy = y - cameraY;
        double dz = z - cameraZ;
        return dx * dx + dy * dy + dz * dz;
    }
}
