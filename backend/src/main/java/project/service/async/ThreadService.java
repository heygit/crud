package project.service.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

interface ThreadService {
    <T> Future<T> execute(Callable<T> task);
}