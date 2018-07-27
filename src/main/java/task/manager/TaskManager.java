package task.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import task.base.BaseTask;

/***
 * @author JadonYuen
 * ÿһ��socket client ��Ӧһ���������
 * ���̳߳�����Ϊȫ��ʹ�� 
 * */
public class TaskManager {
	
	//�ڲ��ֻ࣬��Ϊ���ݴ��ݺʹ���淶ʹ��
	class TaskInfo{
		
		private Future<Integer> future;
		
		private BaseTask baseTask;
		
		public TaskInfo(Future<Integer> future,BaseTask baseTask) {
			this.future = future;
			this.baseTask = baseTask;
		}
		
		public Future<Integer> getFuture(){
			return this.future;
		}
		
		public BaseTask getBaseTask() {
			return this.baseTask;
		}
	}
	
	//ȫ���̳߳�
	private static ExecutorService executorService  = Executors.newCachedThreadPool();
	
	private Map<String,TaskInfo> taskMap = new HashMap<String, TaskInfo>();
	
	public TaskManager() { 
		//�����̳߳�
//		FutureTask<V>
	}

	/***
	 * �������
	 * @param task
	 * @param taskId
	 */
	public void addTask(BaseTask task) {
		Future future = executorService.submit(task);
		this.taskMap.put(task.getTaskId(), new TaskInfo(future,task));
	}
	
	public BaseTask getTask(String taskId) {
		TaskInfo taskInfo = this.taskMap.get(taskId);
		if(taskInfo != null)return taskInfo.baseTask;
		else return null;
	}
	 
	/***
	 * �Ƴ�����
	 * @param taskId
	 * @throws Exception 
	 */
	public void removeTask(String taskId){
		
		TaskInfo taskInfo = this.taskMap.get(taskId);
		if(taskInfo != null) {
			taskInfo.getFuture().cancel(true);
			this.taskMap.remove(taskId);
		}
	}
	
	public void stopTask(String taskId) throws Exception {
		TaskInfo taskInfo = this.taskMap.get(taskId);
		if(taskInfo != null) {
			taskInfo.getFuture().cancel(true);
			taskInfo.getBaseTask().cancelTask();
			this.taskMap.remove(taskId);
		}
	}
	
	public void shutdown() {
		this.executorService.shutdown();
		Set<String> set = this.taskMap.keySet();
		Iterator<String> iterator = set.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next();
			TaskInfo taskInfo = this.taskMap.get(key);
			taskInfo.getFuture().cancel(true);
			this.taskMap.remove(key);
			iterator.remove();
		}
	}
	
}
