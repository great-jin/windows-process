package xyz.ibudai.process.common;

public class FormConst {

    public static final String BLANK = "";
    public static final int FRAME_WIDTH = 1200;
    public static final int FRAME_HEIGHT = 600;

    public static class Zh {
        public static final String TITLE = "Windows 进程管理";

        // 标签
        public static final String LB_PORT = "端口:";
        public static final String LB_PID = "进程号:";

        // 按钮
        public static final String BT_SEARCH = "搜索";
        public static final String BT_RESET = "重置";
        public static final String BT_KILL = "结束";

        // 提示
        public static final String MSG_INPUT_PID = "请输入 PID";
        public static final String MSG_INPUT_PORT = "请输入端口号";
        public static final String MSG_PROCESS_CLOSE = "进程关闭成功";

        // 标题
        public static final String MSG_TITLE_ERROR = "Input Error";
        public static final String MSG_TITLE_SUCCESS = "Successfully";
        public static final String MSG_TITLE_UNEXPECT = "Unexpect Error";
    }

    public static class En {
        public static final String TITLE = "Windows Process";
        public static final String LB_PORT = "Port:";
        public static final String LB_PID = "PID:";
        public static final String BT_SEARCH = "Search";
        public static final String BT_RESET = "Reset";
        public static final String BT_KILL = "Kill";
    }
}
