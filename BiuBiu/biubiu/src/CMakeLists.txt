cmake_minimum_required(VERSION 3.4.1)

add_library(
            bsdiff

            SHARED

            src/main/cpp/bzip2-1.0.6/compress.c
             src/main/cpp/bzip2-1.0.6/decompress.c
             src/main/cpp/bzip2-1.0.6/bzlib.h
             src/main/cpp/bzip2-1.0.6/bzlib_private.h
             src/main/cpp/bzip2-1.0.6/huffman.c
             src/main/cpp/bzip2-1.0.6/blocksort.c
             src/main/cpp/bzip2-1.0.6/randtable.c
             src/main/cpp/bzip2-1.0.6/bzlib.c
             src/main/cpp/bzip2-1.0.6/crctable.c
             src/main/cpp/bsdiff.c
             src/main/cpp/bspatch.c
             src/main/jni/com_im_hero_diffpatch_BsDiff.c
             src/main/jni/com_im_hero_diffpatch_BsDiff.h
             src/main/jni/im_hero_log.h)

find_library(
log-lib
log
)

target_link_libraries(
bsdiff
${log-lib}
)
