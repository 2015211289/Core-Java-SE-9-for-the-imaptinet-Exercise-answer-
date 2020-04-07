#! /Library/Java/JavaVirtualMachines/jdk-12.0.1.jdk/Contents/Home/bin/jjs

//该脚本采用Nashorn的JavaSc引擎

//exercise9
var pathToRoot = readLine('directory: ')
var zipPath = Paths.get(pathToRoot + '.jar')
var uri = new java.net.URI('jar', zipPath.toUri().toString(), null)
try {
    var zipfs = java.nio.file.FilesSystems.newFileSystem(uri,
        java.util.Collections.singletonMap('create', 'true'))
    var reg = RegExp(/.+\.class/);
    var path = java.nio.file.Paths(pathToRoot)
    var entries = java.nio.file.Files.walk(path)
    entries.filter(function (x) {
        return x.toString().match(reg)
    }).forEach(function (x) {
        java.nio.file.Files.copy(x, zipfs.getPath("/").resolve(x))
    })
} catch (e) {
    print('error in reading files')
}

//exercise10
var env = $ENV
for (var t in env) {
    print(t + ':' + env[t])
}

//exercise11
var age = null
if ($ARG[0]) age = $ARG[0]
else if ($ENV.AGE) age = $ENV.AGE
else age = readLine('age:')

print('Next year, you will be ${age+1}')


