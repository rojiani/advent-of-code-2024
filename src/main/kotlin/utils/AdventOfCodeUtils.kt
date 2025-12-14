package com.rojiani.utils

import java.io.File

const val INPUTS_DIR = "src/main/resources/inputs"

fun readInputText(filename: String): String = File("$INPUTS_DIR/$filename").readText(Charsets.UTF_8)
