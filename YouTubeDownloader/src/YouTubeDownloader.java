import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class YouTubeDownloader {

    private static JButton downloadButton; // ���� ������ ����
    private static JLabel resultLabel;
    private static JTextField urlField; // urlField�� ���� ������ ����

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        // JFrame ����
        JFrame frame = new JFrame("YouTube Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);  // â ũ�� ����
        frame.setLocationRelativeTo(null); // ȭ�� �߾ӿ� ��ġ

        // Panel ����
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // ���� �߰�

        // YouTube ��ũ �Է� �ʵ�
        JLabel urlLabel = new JLabel("�ٿ�ε��� YouTube ��ũ�� �Է��ϼ���:");
        urlField = new JTextField(25); // urlField�� ���� ������ ����
        urlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        urlField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        urlField.setPreferredSize(new Dimension(350, 30)); // ���� ���̱�
        urlField.setMaximumSize(new Dimension(350, 30));  // �ִ� ũ�� ����

        // URL �Է� �ʵ� ����
        urlLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // ���Ʒ� ����
        urlField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // �¿� ����

        // �ٿ�ε� �ɼ� ���� ��ư
        JRadioButton option1 = new JRadioButton("��ǰ�� ����");
        JRadioButton option2 = new JRadioButton("��ǰ�� �����");

        // ���� ��ư �׷�
        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);

        // ���� ��ư���� FlowLayout�� ����Ͽ� �� �ٿ� ��ġ
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // �߾� ����, ��ư ���� ����
        radioPanel.add(option1);
        radioPanel.add(option2);
        
        // �⺻ �ɼ� ���� (���� ���� ����)
        option1.setSelected(true);
        option1.setAlignmentX(Component.CENTER_ALIGNMENT);
        option2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // �ٿ�ε� ��ư�� �ݱ� ��ư�� ���� �ٿ� ��ġ�� �г�
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // �߾� ����, ��ư ���� ����
        downloadButton = new JButton("�ٿ�ε�"); // ���� ������ ����� downloadButton ���
        JButton closeButton = new JButton("�ݱ�");

        // �ٿ�ε� ��ư Ŭ�� ��
        downloadButton.addActionListener(e -> {
            String videoUrl = urlField.getText().trim();
            if (videoUrl.isEmpty()) {
                resultLabel.setText("��ũ�� �Է����ּ���.");
                return;
            }

            // ���õ� �ɼǿ� �´� ����
            String formatOption = option1.isSelected() ? "bestvideo" : "bestaudio"; // ���� �Ǵ� ����� �� �ϳ��� ����

            // ���� �ð��� �����ͼ� ���� �̸� �ڿ� �߰� (yyyyMMddHHmmss ����)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // �ٿ�ε� ��� ���� (���� �̸��� timestamp �߰�)
            String downloadLocation = System.getProperty("user.home") + "\\Desktop\\YoutubeDownload\\%(title)s_" + timestamp + ".%(ext)s";

            // �ٿ�ε� ����
            downloadButton.setText("������..."); // ��ư �ؽ�Ʈ ����
            downloadVideo(videoUrl, downloadLocation, formatOption);
        });

        // �ݱ� ��ư Ŭ�� ��
        closeButton.addActionListener(e -> System.exit(0));

        // �ٿ�ε� ��ư�� �ݱ� ��ư�� buttonPanel�� �߰�
        buttonPanel.add(downloadButton);
        buttonPanel.add(closeButton);

        // ��� ��� ��
        resultLabel = new JLabel("");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // UI ������Ʈ ��ġ
        panel.add(urlLabel);
        panel.add(urlField);
        panel.add(radioPanel);
        panel.add(buttonPanel);   // �ٿ�ε� �� �ݱ� ��ư�� ���Ե� ��ư �г�
        panel.add(resultLabel);   // ��� �� (�ٿ�ε� �Ϸ� �޽���)

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void downloadVideo(String videoUrl, String downloadLocation, String formatOption) {
        try {
            // ���� ���丮���� yt-dlp.exe ��� ���� (���ÿ��� �׽�Ʈ �ÿ��� ���)
            String currentDirectory = System.getProperty("user.dir");
            File ytDlpExe = new File(currentDirectory, "yt-dlp.exe");

            // ���� ��ΰ� ���ÿ� ���� �� �׽�Ʈ�� ��� ���� (���� ���� ��θ� �ϵ��ڵ�)
            // String ytDlpPath = "C:\\Users\\G\\Desktop\\yt-dlp.exe";  // �ӽ� ���� ��� (�׽�Ʈ��)

            // ���� ������ ��� ���� (�ּ��� Ǯ�� �� �ڵ带 ����� ���� �Ʒ��� ���� ���)
            String ytDlpPath = ytDlpExe.getAbsolutePath();  // ���� ��η� ���

            if (new File(ytDlpPath).exists()) {
                // yt-dlp ��ΰ� �����ϴ� ���, ��ɾ� ����
                String command = ytDlpPath + " -f " + formatOption + " -o \"" + downloadLocation + "\" " + videoUrl;

                // ProcessBuilder�� yt-dlp ����
                ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
                processBuilder.inheritIO();  // �ܼ� ��� ���� (���� ����� �ֿܼ� ���)

                Process process = processBuilder.start(); // ���μ��� ����

                // ���μ����� �Ϸ�� ������ ��ٸ��ϴ�.
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    SwingUtilities.invokeLater(() -> {
                        resultLabel.setText("�ٿ�ε� �Ϸ�!");
                        downloadButton.setText("�ٿ�ε�"); // �Ϸ� �� ��ư �ؽ�Ʈ �ʱ�ȭ

                        // �ٿ�ε� �Ϸ� �� URL �Է� �ʵ带 �ʱ�ȭ
                        urlField.setText(""); // ��ũ �Է� �ʵ带 �ʱ�ȭ
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        resultLabel.setText("�ٿ�ε� �� ���� �߻�!");
                        downloadButton.setText("�ٿ�ε�");
                    });
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    resultLabel.setText("yt-dlp.exe ������ ã�� �� �����ϴ�.");
                    downloadButton.setText("�ٿ�ε�");
                });
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                resultLabel.setText("�ٿ�ε� �� ���� �߻�!");
                downloadButton.setText("�ٿ�ε�");
            });
        }
    }
}
