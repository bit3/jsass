
pushd
cd ../src/main/libsass
set BUILD=shared
make lib/libsass.dll
copy lib\libsass.dll ..\resources\win32-x86-64\sass.dll
popd