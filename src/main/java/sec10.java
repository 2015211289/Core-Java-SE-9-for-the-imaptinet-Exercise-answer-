import java.io.File;
import java.io.IOException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class sec10 {

    //exercise1
    static void exercise1(String dir, String word) {
        try (Stream<Path> paths = Files.walk(Paths.get(dir))) {
            Optional<Path> file =
                    paths.parallel().filter(Files::isRegularFile).filter(p -> {
                        try (Scanner in = new Scanner(p, StandardCharsets.UTF_8)) {
                            in.useDelimiter("\\PL+");
                            return in.tokens().anyMatch(s -> s.equals(word));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    }).findAny();
            file.ifPresentOrElse(System.out::println, () -> System.out.println(
                    "Not Found"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    /*
    使用findAny()，只会找到任何匹配项，然后结束并行流的计算。
    通过parallel()，把流转换成并行流，并发访问文件。
     */

    //exercise2
    /*
    在int[]长度为100000时，并行排序时间平均少于非并行排序。
     */

    //exercise3
    static public Callable<Path> readFile(Path file, String word) {
        Callable<Path> task = () -> {
            Scanner in = new Scanner(file, StandardCharsets.UTF_8);
            in.useDelimiter("\\PL+");
            while (in.hasNext()) {
                if (Thread.currentThread().isInterrupted()) {
                    System.err.println(file + " is Interrupted");
                    throw new Exception("Interrupted");
                }
                String s = in.next();
                if (s.equals(word)) return file;
            }
            throw new Exception("not found");
        };
        return task;
    }

    static void exercise3(String dir, String word) throws IOException {
        List<Callable<Path>> tasks = new ArrayList<>();
        Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
            tasks.add(readFile(p, word));
        });
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Path result = executor.invokeAny(tasks);
            System.out.println(result);
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }
    /*
    程序执行完，executor要等一会才会终止。
     */

    //exercise4
    static class Matrix {
        public long a, b, c, d;

        public Matrix(long x, long y, long z, long w) {
            a = x;
            b = y;
            c = z;
            d = w;
        }

        public Matrix multiply(Matrix o) {
            return new Matrix(a * o.a + b * o.c, a * o.b + b * o.d, c * o.a + d * o.c, c * o.b + d * o.d);
        }
    }

    static public long exercise4(int n) {
        Matrix[] matrices = new Matrix[n];
        Arrays.parallelSetAll(matrices, i -> {
            return new Matrix(1, 1, 1, 0);
        });
        Arrays.parallelPrefix(matrices, Matrix::multiply);
        return matrices[n - 1].a;
    }

    //exercise5
    static class Stable {
        private final int id;

        public Stable(int x) {
            id = x;
        }

        public final Stable changeID(int s) {
            return new Stable(s);
        }

        public int getID() {
            return id;
        }
    }
    /*
    没有在构造函数里将this传递给其他函数。
     */

    //exercise6
    static void exercise6(String dir) throws Exception {
        ConcurrentHashMap<String, Set<File>> map = new ConcurrentHashMap<>();
        List<Callable<Boolean>> tasks = new ArrayList<>();

        Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
            tasks.add(() -> {
                try {
                    Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    in.tokens().forEach(s -> map.merge(s,
                            new HashSet<File>() {{
                                this.add(p.toFile());
                            }},
                            (exist, newo) -> {
                                exist.addAll(newo);
                                return exist;
                            }));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return true;

            });
        });
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.invokeAll(tasks);
        System.out.println(map);
    }

    //exercise7
    static void exercise7(String dir) throws Exception {
        ConcurrentHashMap<String, Set<File>> map = new ConcurrentHashMap<>();
        List<Callable<Boolean>> tasks = new ArrayList<>();

        Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
            tasks.add(() -> {
                try {
                    Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    in.tokens().forEach(s -> map.computeIfAbsent(s, (k) -> {
                        return new HashSet<>();
                    }).add(p.toFile()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return true;

            });
        });
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.invokeAll(tasks);
        System.out.println(map);
    }
    /*
    computeIfAbsent不需要额外的初始化语句，不用考虑是否存在的情况。
     */

    //exercise8
    static String exercise8(ConcurrentHashMap<String, Long> map) {
        Map.Entry<String, Long> result = map.reduceEntries(10000, (entry1, entry2) -> {
            if (entry1.getValue() > entry2.getValue()) return entry1;
            else return entry2;
        });
        return result.getKey();
    }

    //exercise9
    static void exercise9a() {
        AtomicLong nextNumber = new AtomicLong();
        ExecutorService executor = Executors.newCachedThreadPool();
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 100000; j++) nextNumber.incrementAndGet();
            });
        }
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            long r = nextNumber.get();
            long e = System.currentTimeMillis();
            System.out.println(e - s);
            System.out.println(r);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    static void exercise9b() {
        LongAdder count = new LongAdder();
        ExecutorService executor = Executors.newCachedThreadPool();
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 100000; j++) count.increment();
            });
        }
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            long r = count.sum();
            long e = System.currentTimeMillis();
            System.out.println(e - s);
            System.out.println(r);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    /*
    使用AtomicLong的时间为1770ms，使用LongAddr的时间为374ms。
    LongAddr的性能更好。
     */

    //exercise10
    static void exercise10() {
        LongAccumulator max = new LongAccumulator(Math::max, Long.MIN_VALUE);
        LongAccumulator min = new LongAccumulator(Math::min, Long.MAX_VALUE);
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                Random random = new Random();
                max.accumulate(random.nextInt());
                min.accumulate(random.nextInt());
            });
        }
        try {
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            System.out.println(max.get() + "\n" + min.get());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    //exercise11
    static void exercise11(String dir, String word) {
        BlockingQueue<Path> queue = new LinkedBlockingQueue<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                    try {
                        queue.put(p);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                });
                queue.put(Paths.get(dir + "/temp"));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                Path path = queue.take();
                while (!path.toString().equals(dir + "/temp")) {
                    Scanner in = new Scanner(path, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    boolean r = in.tokens().parallel().anyMatch(s -> s.equals(word));
                    if (r) System.out.println(path);
                    path = queue.take();
                }
                queue.put(path);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        executor.shutdown();
        try {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    //exercise12
    static void exercise12(String dir) {
        BlockingQueue<Path> queue = new LinkedBlockingQueue<>();
        BlockingQueue<Map<String, Long>> words = new LinkedBlockingQueue<>();
        Map<String, Long> total = new HashMap<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                    try {
                        queue.put(p);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        ex.printStackTrace();
                    }
                });
                queue.put(Paths.get(dir + "/temp"));
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        executor.submit(() -> {
            try {
                Path path = queue.take();
                while (!path.toString().equals(dir + "/temp")) {
                    Scanner in = new Scanner(path, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    HashMap<String, Long> map = new HashMap<>();
                    in.tokens().parallel().forEach(w -> {
                        map.merge(w, 1L, Long::sum);
                    });
                    words.put(map);
                    path = queue.take();
                }
                queue.put(path);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                Map<String, Long> map = words.poll(100, TimeUnit.MILLISECONDS);
                while (map != null) {
                    for (Map.Entry<String, Long> x : map.entrySet()) {
                        String s = x.getKey();
                        Long l = x.getValue();
                        total.merge(s, l, Long::sum);
                    }
                    map = words.poll(100, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
        });
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(total);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    /*
    因为每个消费者将独立的映射插入队列，不存在共享，所以不需要ConcurrentHashMap。
     */

    //exercise13
    static void exercise13(String dir) {
        ArrayList<Callable<Map<String, Integer>>> tasks = new ArrayList<>();
        try {
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                tasks.add(() -> {
                    Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    HashMap<String, Integer> map = new HashMap<>();
                    in.tokens().parallel().forEach(w -> {
                        map.merge(w, 1, Integer::sum);
                    });
                    return map;
                });
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            List<Future<Map<String, Integer>>> results = executor.invokeAll(tasks);
            Map<String, Integer> total = new HashMap<>();
            for (Future<Map<String, Integer>> f : results) {
                for (Map.Entry<String, Integer> entry : f.get().entrySet()) {
                    total.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
            System.out.println(total);

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }
    /*
    因为每个任务返回独立的map，没有共享，所以不需要ConcurrentHashMap。
     */

    //exercise14
    static void exercise14(String dir) {
        ArrayList<Callable<Map<String, Integer>>> tasks = new ArrayList<>();
        try {
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                tasks.add(() -> {
                    Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    Map<String, Integer> map = new HashMap<>();
                    in.tokens().parallel().forEach(w -> {
                        map.merge(w, 1, Integer::sum);
                    });
                    return map;
                });
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ExecutorService executor = Executors.newCachedThreadPool();
        ExecutorCompletionService<Map<String, Integer>> service
                = new ExecutorCompletionService(executor);
        try {
            for (Callable<Map<String, Integer>> task : tasks) {
                service.submit(task);
            }
            Map<String, Integer> total = new HashMap<>();
            for (int i = 0; i < tasks.size(); i++) {
                for (Map.Entry<String, Integer> entry : service.take().get().entrySet()) {
                    total.merge(entry.getKey(), entry.getValue(), Integer::sum);
                }
            }
            System.out.println(total);

        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    //exercise15
    static void exercise15(String dir) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                executor.submit(() -> {
                    try {
                        Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                        in.useDelimiter("\\PL+");
                        in.tokens().parallel().forEach(w -> {
                            map.merge(w, 1, Integer::sum);
                        });
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                });
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(map);

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    //exercise16
    static void exercise16(String dir) {
        try {
            Map<String, Long> map = Files.walk(Paths.get(dir)).parallel().
                    filter(Files::isRegularFile).flatMap(p -> {
                try {
                    Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                    in.useDelimiter("\\PL+");
                    return in.tokens();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return Stream.empty();
                }
            }).collect(Collectors.groupingBy(s -> s,
                    Collectors.counting()));
            System.out.println(map);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //exercise17
    public static long count = 0;

    static void exercise17(String dir) {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                Runnable task = () -> {
                    try {
                        Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                        in.useDelimiter("\\PL+");
                        count += in.tokens().count();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                };
                executor.submit(task);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println(count);
    }
    /*
    对static变量的改变是竞争的，所以结果可能不一致。出现错误答案。
     */

    //exercise18
    static void exercise18(String dir) {
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Lock countlock = new ReentrantLock();
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                Runnable task = () -> {
                    try {
                        Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                        in.useDelimiter("\\PL+");
                        countlock.lock();
                        count += in.tokens().count();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        countlock.unlock();
                    }
                };
                executor.submit(task);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println(count);
    }

    //exercise19
    static void exercise19(String dir) {
        final LongAdder count = new LongAdder();
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            Files.walk(Paths.get(dir)).filter(Files::isRegularFile).forEach(p -> {
                Runnable task = () -> {
                    try {
                        Scanner in = new Scanner(p, StandardCharsets.UTF_8);
                        in.useDelimiter("\\PL+");
                        in.tokens().forEach(s -> count.increment());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                };
                executor.submit(task);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        executor.shutdown();
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println(count.sum());
    }

    //exercise20
    /*
    1.当多个线程同时push时，其中一个可能覆盖另一个节点。
    2.当多个线程同时pop时，会导致出栈顺序不对。
     */

    //exercise21
    /*
    1.当一个线程执行第一个元素插入，head=n时，另一个线程执行add操作，却发现tail为null，程序发生异常。
    2.当一个线程执行remove的，另一个线程执行完remove，导致同一个节点出队两次。
     */

    //exercise22
    /*
    上锁的是myLock对象，而不是Stack对象。
     */

    //exercise23
    /*
    他是给一个显示锁对象上了锁，和Stack对象没关系。
     */

    //exercise24
    /*
    只给values上锁，但没有给size上锁，可能会导致size被其他方法修改。
     */

    //exercise25
    static public CompletableFuture<String> readPage(URI url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        CompletableFuture<HttpResponse<String>> f = client.sendAsync(request,
                HttpResponse.BodyHandlers.ofString());
        return f.thenApply(h -> h.body());
    }

    static public List<URI> getlinks(String body) {
        ArrayList<URI> list = new ArrayList<>();
        try {
            System.out.println(body);
            Pattern pattern = Pattern.compile("<a\\s+href\\s*=\\s*['\"]?(" +
                    "[^\\s>]+)['\"]?.*?>.+?</a>");

            Matcher matcher = pattern.matcher(body);
            while (matcher.find()) {
                String match = matcher.group(1);
                list.add(new URI(match));
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    static void exercise25(String url) {
        try {
            ExecutorService executor = Executors.newCachedThreadPool();
            CompletableFuture<String> contents = readPage(new URI(url));
            CompletableFuture<List<URI>> links =
                    contents.thenApply(sec10::getlinks);
            links.thenAccept(list -> {
                for (URI uri : list) {
                    System.out.println(uri);
                }
            });
            executor.awaitTermination(5, TimeUnit.SECONDS);

        } catch (URISyntaxException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    //exercise26
    public static <T> CompletableFuture<T> repeat(Supplier<T> action,
                                                  Predicate<T> untill) {
        CompletableFuture<T> f = CompletableFuture.supplyAsync(() -> {
            T result = action.get();
            return result;
        });
        CompletableFuture<Boolean> b = f.thenApplyAsync(s -> untill.test(s));
        return b.thenCompose(s -> {
            if (s) return f;
            else return repeat(action, untill);
        });
    }


    static void exercise26() {
        PasswordAuthentication users = new PasswordAuthentication("seth",
                "123456".toCharArray());
        Supplier<String> action = () -> {
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            return s;
        };
        Predicate<String> untill = (String s) -> {
            try {
                Thread.sleep(1000);
                String[] strings = s.split("\\s+");
                String user = strings[0];
                String pass = strings[1];
                String p = String.valueOf(users.getPassword());
                if (users.getUserName().equals(user) && p.equals(pass))
                    return true;
                else return false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                Thread.currentThread().interrupt();
            }
            return false;
        };

        CompletableFuture<String> task = repeat(action, untill);
        task.thenAccept(s -> System.out.println("welcome"));
        while (!task.isDone()) ;
    }

    //exercise27
    static <T> CompletableFuture<T> supplyAsync(Supplier<T> action,
                                                Executor exec) {
        CompletableFuture<T> f = new CompletableFuture<>();
        exec.execute(() -> {
            Thread thread = Thread.currentThread();
            T result = action.get();
            f.complete(result);
            if (f.isCancelled()) {
                thread.interrupt();
            }
        });
        return f;
    }

    //exercise28
    static <T> CompletableFuture<List<T>> allof(List<CompletableFuture<T>> cfs) {
        CompletableFuture<List<T>> l = new CompletableFuture<>();
        List<T> list = new ArrayList<>();
        l.complete(list);
        for (CompletableFuture<T> f : cfs) {
            l = f.thenCombine(l, (s, t) -> {
                t.add(s);
                return t;
            });
        }
        return l;
    }

    //exercise29
    static <T> CompletableFuture<T> anyOf(List<Supplier<T>> actions,
                                          Executor exec) {
        CompletableFuture<T> f = new CompletableFuture<>();
        final AtomicLong count = new AtomicLong();
        for (Supplier<T> action : actions) {
            exec.execute(() -> {
                try {
                    f.complete(action.get());
                } catch (Throwable ex) {
                    long t = count.getAndIncrement();
                    if (t == actions.size()) {
                        f.completeExceptionally(new NoSuchElementException());
                    }
                }
            });
        }
        return f;
    }

}
