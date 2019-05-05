package com.example.dames

import kotlin.math.sqrt


fun cross_product(v1: FloatArray, v2: FloatArray) : FloatArray{
    val res = FloatArray(3)
    res[0] = v1[1] * v2[2] - v1[2] * v2[1];
    res[1] = v1[2] * v2[0] - v1[0] * v2[2];
    res[2] = v1[0] * v2[1] - v1[1] * v2[0];
    return res;
}

fun normalize(v : FloatArray) : FloatArray{
    var norm : Float = sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2])
    return floatArrayOf(v[0] / norm, v[1] / norm, v[2] / norm)
}

fun triangle_normal(v1 : FloatArray, v2: FloatArray, v3: FloatArray) : FloatArray{
    val v : FloatArray = floatArrayOf(v2[0] - v1[0], v2[1] - v1[1], v2[2] - v1[2] )
    val u : FloatArray = floatArrayOf(v3[0] - v1[0], v3[1] - v1[1], v3[2] - v1[2] )
    return normalize(cross_product(v, u))

}