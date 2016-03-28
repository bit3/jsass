include(CMakeForceCompiler)

# the name of the target operating system
SET(CMAKE_SYSTEM_NAME Darwin)

SET(CMAKE_OSX_SYSROOT /opt/osxcross/target/SDK/MacOSX10.8.sdk)

# which compilers to use for C and C++
SET(CMAKE_C_COMPILER x86_64-apple-darwin12-clang)
SET(CMAKE_CXX_COMPILER x86_64-apple-darwin12-clang++-libc++)
SET(CMAKE_AR x86_64-apple-darwin12-ar CACHE FILEPATH "Archiver")

# here is the target environment located
SET(CMAKE_FIND_ROOT_PATH  /opt/osxcross/target/SDK/MacOSX10.8.sdk/usr/include)

# adjust the default behaviour of the FIND_XXX() commands:
# search headers and libraries in the target environment, search
# programs in the host environment
set(CMAKE_FIND_ROOT_PATH_MODE_PROGRAM NEVER)
set(CMAKE_FIND_ROOT_PATH_MODE_LIBRARY BOTH)
set(CMAKE_FIND_ROOT_PATH_MODE_INCLUDE BOTH)
