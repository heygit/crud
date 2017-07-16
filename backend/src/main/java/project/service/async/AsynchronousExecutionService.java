package project.service.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Service
public class AsynchronousExecutionService implements ThreadService {

    private TaskExecutor taskExecutor;

    @Autowired
    public AsynchronousExecutionService(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
    
    public <T> Future<T> execute(Callable<T> task){
        FutureTask<T> result = new FutureTask<>(task);

        taskExecutor.execute(result);

        return result;
    }
}