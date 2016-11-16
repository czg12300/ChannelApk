
package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    private static ExecutorService mExecutorService = Executors.newCachedThreadPool();

    // private JFrame frame;
    private JTextField fApkPath;

    private JTextField fQd;

    private JTextField fOut;

    private JTextField fChannelIdFlag;

    private JLabel lProgress;

    private JFileChooser fileChooser;

    private JFileChooser dirChooser;

    private JCheckBox jc;

    private boolean isDeleteAllFile = false;

    private int height = 20;

    private int left = 20;

    private int right = 20;

    private JButton btnStart;

    private JButton btnFinish;

    private StringBuilder logBuilder = new StringBuilder();

    private void addLine() {
        height += 50;
    }

    /**
     * Create the application.
     */
    public MainFrame() {
        init();
    }

    private void init() {

        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                if (file.isFile()) {
                    String fileName = file.getName();
                    String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if (extension != null) {
                        if (extension.equals("apk") || extension.equals("properties")) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public String getDescription() {
                return "apk文件或者配置文件(*.apk, *.properties)";
            }
        });
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设置选择模式，既可以选择文件又可以选择文件夹
        dirChooser = new JFileChooser();
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);// 设置选择模式，既可以选择文件又可以选择文件夹
        setBounds(50, 100, 900, 800);
        left = 200;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel ltitle = new JLabel("多渠道打包系统");
        ltitle.setBounds(getWidth()/2-120, height, 555, 25);
        height += 30;
        addLine();

        JLabel lapk = new JLabel("原始apk");
        lapk.setBounds(left, height, left + 60, 25);
        fApkPath = new JTextField();
        fApkPath.setBounds((int) (lapk.getX() + 80), height, 299, 25);
        JButton btnApk = new JButton("选择");
        btnApk.setBounds((int) (fApkPath.getWidth() + fApkPath.getX() + 20), height, 87, 26);
        btnApk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = fileChooser.showOpenDialog(null);
                fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setAcceptAllFileFilterUsed(false);
                dirChooser.setCurrentDirectory(fileChooser.getSelectedFile());
                if (index == JFileChooser.APPROVE_OPTION) {
                    // 把获取到的文件的绝对路径显示在文本编辑框中
                    fApkPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        addLine();

        JLabel lqd = new JLabel("渠道号文件");
        lqd.setBounds(left - 25, height, left + 60, 25);
        fQd = new JTextField();
        fQd.setBounds((int) (lapk.getX() + 80), height, 299, 25);
        JButton btnQd = new JButton("选择");
        btnQd.setBounds((int) (fQd.getWidth() + fQd.getX() + 20), height, 87, 26);
        btnQd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = fileChooser.showSaveDialog(null);
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setMultiSelectionEnabled(false);
                fileChooser.setAcceptAllFileFilterUsed(false);
                dirChooser.setCurrentDirectory(fileChooser.getSelectedFile());
                if (index == JFileChooser.APPROVE_OPTION) {
                    // 把获取到的文件的绝对路径显示在文本编辑框中
                    fQd.setText(fileChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        addLine();

        JLabel lout = new JLabel("输出文件夹");
        lout.setBounds(left - 25, height, left + 60, 25);
        fOut = new JTextField();
        fOut.setBounds((int) (lapk.getX() + 80), height, 299, 25);
        JButton btnOut = new JButton("选择");
        btnOut.setBounds((int) (fOut.getWidth() + fOut.getX() + 20), height, 87, 26);
        btnOut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = dirChooser.showSaveDialog(null);
                dirChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                dirChooser.setMultiSelectionEnabled(false);
                dirChooser.setAcceptAllFileFilterUsed(false);
                if (index == JFileChooser.APPROVE_OPTION) {
                    fOut.setText(dirChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        jc = new JCheckBox("清除输出文件夹里面的所有文件");
        jc.setBounds((int) (lapk.getX() + 80), height + 40, 555, 26);
        jc.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                if (ie.getStateChange() == ItemEvent.SELECTED) {
                    isDeleteAllFile = true;
                } else {
                    isDeleteAllFile = false;
                }

            }
        });
        addLine();
        addLine();

        JLabel lChannelId = new JLabel("渠道号替换符");
        lChannelId.setBounds(left - 40, height, 133, 25);
        fChannelIdFlag = new JTextField();
        fChannelIdFlag.setBounds((int) (lapk.getX() + 80), height, 299, 25);

       
        addLine();
        btnFinish = new JButton("打开输出文件夹");
        btnFinish.setBounds(getWidth() / 2 - 100, height, 160, 34);
        btnFinish.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().open(new File(fOut.getText()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        btnFinish.setVisible(false);
        addLine();
        btnStart = new JButton("打包");
        btnStart.setEnabled(true);
        btnStart.setBounds(getWidth() / 2 - 100, height, 160, 34);
        btnStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnStart.setText("打包中");
                btnStart.setEnabled(false);
                btnFinish.setVisible(false);
                mExecutorService.execute(new Runnable() {

                    @Override
                    public void run() {
                        startWriteComment();
                    }
                });

            }
        });
        addLine();
        lProgress = new JLabel();
        JScrollPane jsp=new JScrollPane(lProgress);
        jsp.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setBounds(160, height, 560, getHeight()-height-60);
        getContentPane().add(jsp); 
        setTextSize(ltitle, 26);
        setTextSize(jc);
        setTextSize(lChannelId);
        setTextSize(fChannelIdFlag);
        setTextSize(fApkPath);
        setTextSize(fQd);
        setTextSize(fOut);
        setTextSize(lapk);
        setTextSize(lqd);
        setTextSize(lout);
        setTextSize(btnFinish, 16);
        setTextSize(btnStart, 16);
        setTextSize(btnQd);
        setTextSize(btnOut);
        setTextSize(btnApk);
        setTextSize(lProgress, 14);

        getContentPane().add(btnFinish);
        getContentPane().add(lChannelId);
//        getContentPane().add(lProgress);
        getContentPane().add(jc);
        getContentPane().add(ltitle);
        getContentPane().add(fChannelIdFlag);
        getContentPane().add(fApkPath);
        getContentPane().add(fQd);
        getContentPane().add(fOut);
        getContentPane().add(lapk);
        getContentPane().add(lqd);
        getContentPane().add(lout);
        getContentPane().add(btnApk);
        getContentPane().add(btnQd);
        getContentPane().add(btnOut);
        getContentPane().add(btnStart);
    }

    private void resetStartBtn() {
        btnStart.setEnabled(true);
        btnStart.setText("打包");
        // fApkPath.setText("");
        // fQd.setText("");
        // fOut.setText("");
        // fChannelIdFlag.setText("");
    }

    protected void startWriteComment() {
        final String pApkPath = fApkPath.getText();
        final String pQd = fQd.getText();
        final String pOut = fOut.getText();
        final String pChannelIdFlag = fChannelIdFlag.getText();
        if (IoUtils.isEmpty(pApkPath)) {
            log("请选择原始apk文件");
            resetStartBtn();
            return;
        }
        if (IoUtils.isEmpty(pQd)) {
            log("请选择渠道号文件");
            resetStartBtn();
            return;
        }
        if (IoUtils.isEmpty(pOut)) {
            log("请选择输出文件夹");
            resetStartBtn();
            return;
        }
        if (IoUtils.isEmpty(pChannelIdFlag)) {
            log("请输入渠道号替换符");
            resetStartBtn();
            return;
        }
        log("开始解析渠道号文件");
        PropertiesHelper helper = new PropertiesHelper(pQd);
        if (!helper.isLoadSuccess()) {
            log("解析渠道号文件失败,打包失败");
            resetStartBtn();
            return;
        }
        if (!ApkCommentHelper.isApkFile(pApkPath)) {
            log("选择的原始apk文件不存在或者不是apk文件,打包失败");
            resetStartBtn();
            return;
        }

        final String orgApkName = ApkCommentHelper.getFileName(pApkPath);
        if (IoUtils.isEmpty(orgApkName)) {
            log("获取原始apk文件名失败,打包失败");
            resetStartBtn();
            return;
        } else if (!orgApkName.contains(pChannelIdFlag)) {
            log("获取原始apk文件名中没有渠道号替换符,打包失败");
            resetStartBtn();
            return;
        } else {
            log("获取原始apk文件名成功,文件名为：" + orgApkName);
        }
        if (isDeleteAllFile) {
            IoUtils.deleteDir(pOut);
        }
        log("开始写入渠道号");
        long startTime = System.currentTimeMillis();
        ArrayList<String> keys = helper.getKeyList();
        if (keys != null && keys.size() > 0) {
            log("正在写入渠道号");
            boolean isSuccess=true;
            ArrayList<String> newFileList=new ArrayList<>();
            for (String key : keys) {
                final String channelId = helper.getValue(key);
                String newApkName = orgApkName.replace(pChannelIdFlag, key);
                final String newApkPath = pOut + File.separator + newApkName + ".apk";
                newFileList.add(newApkPath);
                if (ApkCommentHelper.copyFile(pApkPath, newApkPath)) {
                    log("渠道号\"" + channelId + "\" 的apk文件copy成功");
                    if (ApkCommentHelper.writeApk(newApkPath, channelId)) {
                        log("渠道号\"" + channelId + "\" 的apk文件渠道号写入成功");
                    } else {
                        isSuccess=false;
                        log("渠道号\"" + channelId + "\" 的apk文件渠道号写入失败");
                    }
                } else {
                    isSuccess=false;
                    log("渠道号为\"" + channelId + "\" 的apk文件copy失败");
                }
            }
            long endTime = System.currentTimeMillis();
            if(!isSuccess){
                log("打包失败,正在删除copy文件");
                for(String path:newFileList){
                    IoUtils.deleteFile(path);
                }
                log("打包失败,copy文件删除成功");
                btnFinish.setVisible(false);
            }else{
                btnFinish.setVisible(true);
            }
            log((isSuccess?("打包成功!"):"打包失败！")+"    耗时：" + (endTime - startTime) / 1000 + "s");
            
            resetStartBtn();
            lProgress.setText("<html>" + logBuilder.toString() + "</html>");
            logBuilder.delete(0, logBuilder.length());
        } else {
            log("渠道号文件里面没有渠道信息，打包失败");
        }

    }

    private void log(String msg) {
        lProgress.setText(msg);
        logBuilder.append(msg);
        logBuilder.append("<br/>");
    }

    private void setTextSize(JComponent jc) {
        setTextSize(jc, 18);
    }

    private void setTextSize(JComponent jc, int size) {
        jc.setFont(new Font("宋体", Font.BOLD, size));
    }
}
