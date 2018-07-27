package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import converter.base.IConverter;
import converter.cmd.ConveterCommand;
import converter.cmd.ResposeCode;
import converter.controller.ConverterController;
import task.ConverterTask;
import task.base.BaseTask;
import task.base.ITaskCallback;
import task.manager.TaskManager;
import utils.JSONUtils;

/***
 * 
 * @author Socket ���ӵĿͻ���ʵ��
 *
 */
public class SocketClient implements Runnable {

	private Socket socket = null;

	private String socketId = null;

	private boolean isConnect = false;

	private TaskManager taskMgr = new TaskManager();

	private ResposeCode responseCode = new ResposeCode();

	private PrintStream out = null;

	public SocketClient(Socket socket) {
		this.socket = socket;
	}

	public String getSocketId() {
		return this.socketId;
	}

	public void run() {
		this.isConnect = true;
		BufferedReader buf = null;
		try {
			// ��ȡSocket���������������ͻ��˷�������
			out = new PrintStream(this.socket.getOutputStream());
			// ��ȡSocket�����������������մӿͻ��˷��͹���������
			buf = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			while (this.isConnect) {
				// ���մӿͻ��˷��͹���������
				String jsonStr = buf.readLine();
				System.out.println("�յ����Կͻ��˵�һ����Ϣ��");
				JSONObject jsonObj = JSONUtils.parse(jsonStr);
				ConveterCommand cmd = new ConveterCommand(jsonObj);
				String action = cmd.getAction();
				if (action.equals("play")) {
					IConverter converter = ConverterController.getConveter(cmd.getInputPath(), cmd.getOutputPath());
					Map<String, String> externalMap = new HashMap<String, String>();
					externalMap.put("externalId", cmd.getExternalId());
					
					ConverterTask converterTask = new ConverterTask(converter, new ITaskCallback() {
						public void callback(BaseTask task) { // ִ����ϵĻص�
							ConverterTask conTask = (ConverterTask)task;
							Map<String, String> map = new HashMap<String, String>();
							map.put("taskId", conTask.getTaskId());	
							map.put("externalId", conTask.getExternalMap().get("externalId"));	
							if(conTask.getStatus() == 1) { //�������
								responseClient(ResposeCode.EXECUTE_SUCCESS, "convert task done", map);
							}else if(conTask.getStatus() == -1) { //�ж�����
								responseClient(ResposeCode.EXECUTE_STOP, "convert task stop",map);
							}
							taskMgr.removeTask(conTask.getTaskId());
						}
					},externalMap);
					
					taskMgr.addTask(converterTask);
					Map<String, String> map = new HashMap<String, String>();
					map.put("taskId", converterTask.getTaskId());
					this.responseClient(ResposeCode.OK, null, map);
				} else if (action.equals("stop")) {
					taskMgr.stopTask(cmd.getTaskId());
				}
			}
			out.close();
			buf.close();
		} catch (SocketException e) { // �Ͽ�����
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			this.responseClient(ResposeCode.ERROR_REQUEST, e.toString());
		} finally {
			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * ��Ӧ�ͻ���
	 */
	private void responseClient(int code, String msg) {
		this.responseClient(code, msg, null);
	}

	private void responseClient(int code, String msg, Map<String, String> map) {
		if (out != null) {
			// �ظ��ͻ����ѽ�������
			responseCode.setBody(code, msg, map);
			out.println(responseCode.toJSONString());
			out.flush();
		}
	}
}
