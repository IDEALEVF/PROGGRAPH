attribute vec4 vPosition;
attribute vec4 vNormale;
uniform mat4 uMVPMatrix;
varying vec4 normale;
void main() {
    gl_Position = uMVPMatrix * vPosition;
    normale = vNormale;
}