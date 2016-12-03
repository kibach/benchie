# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.0.1)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

# set_property(SOURCE <myfile>.S PROPERTY LANGUAGE C)

# set(CMAKE_C_COMPILER "E:\\AndroidSdks\\Sdk\\ndk-bundle\\toolchains\\x86_64-4.9\\prebuilt\\windows-x86_64\\bin\\x86_64-linux-android-gcc-4.9.exe")

set(can_use_assembler TRUE)
enable_language(C ASM)

include_directories(src/main/cpp/tinymembench)
link_directories(src/main/cpp/tinymembench)

# SET(GCC_COVERAGE_LINK_FLAGS    "-lgnustl_static")

SET(CMAKE_EXE_LINKER_FLAGS  "${CMAKE_EXE_LINKER_FLAGS} ${GCC_COVERAGE_LINK_FLAGS}")
SET(ASM_OPTIONS "-x assembler-with-cpp")
SET(CMAKE_ASM_FLAGS "${CFLAGS} ${ASM_OPTIONS}" )

set_property(SOURCE src/main/cpp/tinymembench/aarch64-asm.S PROPERTY COMPILE_FLAGS "-x assembler-with-cpp")
set_property(SOURCE src/main/cpp/tinymembench/arm-neon.S PROPERTY COMPILE_FLAGS "-x assembler-with-cpp")
set_property(SOURCE src/main/cpp/tinymembench/mips-32.S PROPERTY COMPILE_FLAGS "-x assembler-with-cpp")
set_property(SOURCE src/main/cpp/tinymembench/x86-sse2.S PROPERTY COMPILE_FLAGS "-x assembler-with-cpp")
set_property(SOURCE src/main/cpp/tinymembench/asm-opt.c PROPERTY LANGUAGE C)
set_property(SOURCE src/main/cpp/tinymembench/util.c PROPERTY LANGUAGE C)

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             src/main/cpp/native-lib.cpp )

add_library( # Sets the name of the library.
             tmblib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             src/main/cpp/tinymembench/aarch64-asm.S
             src/main/cpp/tinymembench/arm-neon.S
             src/main/cpp/tinymembench/mips-32.S
             src/main/cpp/tinymembench/x86-sse2.S
             src/main/cpp/tinymembench/asm-opt.c
             src/main/cpp/tinymembench/util.c
             src/main/cpp/tinymembench/tmblib.cpp )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

target_link_libraries( # Specifies the target library.
                       tmblib

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )

