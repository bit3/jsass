cmake_minimum_required(VERSION 3.0)
project(jsass_native_adapter)

include_directories(
    "/usr/lib/jvm/java-8-oracle/include"
    "/usr/lib/jvm/java-8-oracle/include/linux"
    "../libsass"
)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES io_bit3_jsass_NativeAdapter.c)
add_library(jsass_native_adapter SHARED ${SOURCE_FILES})
set_property(TARGET jsass_native_adapter PROPERTY C_STANDARD 99)
