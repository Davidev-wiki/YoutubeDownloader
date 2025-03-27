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

    private static JButton downloadButton; // 전역 변수로 선언
    private static JLabel resultLabel;
    private static JTextField urlField; // urlField를 전역 변수로 선언

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    public static void createAndShowGUI() {
        // JFrame 설정
        JFrame frame = new JFrame("YouTube Downloader");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);  // 창 크기 조정
        frame.setLocationRelativeTo(null); // 화면 중앙에 배치

        // Panel 설정
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여백 추가

        // YouTube 링크 입력 필드
        JLabel urlLabel = new JLabel("다운로드할 YouTube 링크를 입력하세요:");
        urlField = new JTextField(25); // urlField를 전역 변수로 선언
        urlLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        urlField.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        urlField.setPreferredSize(new Dimension(350, 30)); // 높이 줄이기
        urlField.setMaximumSize(new Dimension(350, 30));  // 최대 크기 설정

        // URL 입력 필드 여백
        urlLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // 위아래 여백
        urlField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // 좌우 여백

        // 다운로드 옵션 라디오 버튼
        JRadioButton option1 = new JRadioButton("고품질 비디오");
        JRadioButton option2 = new JRadioButton("고품질 오디오");

        // 라디오 버튼 그룹
        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);

        // 라디오 버튼들을 FlowLayout을 사용하여 한 줄에 배치
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 중앙 정렬, 버튼 간격 설정
        radioPanel.add(option1);
        radioPanel.add(option2);
        
        // 기본 옵션 설정 (가장 좋은 비디오)
        option1.setSelected(true);
        option1.setAlignmentX(Component.CENTER_ALIGNMENT);
        option2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 다운로드 버튼과 닫기 버튼을 같은 줄에 배치할 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 중앙 정렬, 버튼 간격 설정
        downloadButton = new JButton("다운로드"); // 전역 변수로 선언된 downloadButton 사용
        JButton closeButton = new JButton("닫기");

        // 다운로드 버튼 클릭 시
        downloadButton.addActionListener(e -> {
            String videoUrl = urlField.getText().trim();
            if (videoUrl.isEmpty()) {
                resultLabel.setText("링크를 입력해주세요.");
                return;
            }

            // 선택된 옵션에 맞는 포맷
            String formatOption = option1.isSelected() ? "bestvideo" : "bestaudio"; // 비디오 또는 오디오 중 하나만 선택

            // 현재 시간을 가져와서 파일 이름 뒤에 추가 (yyyyMMddHHmmss 형식)
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            // 다운로드 경로 설정 (파일 이름에 timestamp 추가)
            String downloadLocation = System.getProperty("user.home") + "\\Desktop\\YoutubeDownload\\%(title)s_" + timestamp + ".%(ext)s";

            // 다운로드 실행
            downloadButton.setText("진행중..."); // 버튼 텍스트 변경
            downloadVideo(videoUrl, downloadLocation, formatOption);
        });

        // 닫기 버튼 클릭 시
        closeButton.addActionListener(e -> System.exit(0));

        // 다운로드 버튼과 닫기 버튼을 buttonPanel에 추가
        buttonPanel.add(downloadButton);
        buttonPanel.add(closeButton);

        // 결과 출력 라벨
        resultLabel = new JLabel("");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // UI 컴포넌트 배치
        panel.add(urlLabel);
        panel.add(urlField);
        panel.add(radioPanel);
        panel.add(buttonPanel);   // 다운로드 및 닫기 버튼이 포함된 버튼 패널
        panel.add(resultLabel);   // 결과 라벨 (다운로드 완료 메시지)

        frame.add(panel);
        frame.setVisible(true);
    }

    public static void downloadVideo(String videoUrl, String downloadLocation, String formatOption) {
        try {
            // 현재 디렉토리에서 yt-dlp.exe 경로 설정 (로컬에서 테스트 시에만 사용)
            String currentDirectory = System.getProperty("user.dir");
            File ytDlpExe = new File(currentDirectory, "yt-dlp.exe");

            // 실제 경로가 로컬에 있을 때 테스트용 경로 지정 (현재 로컬 경로를 하드코딩)
            // String ytDlpPath = "C:\\Users\\G\\Desktop\\yt-dlp.exe";  // 임시 로컬 경로 (테스트용)

            // 실제 배포용 경로 설정 (주석을 풀고 이 코드를 사용할 때는 아래와 같이 사용)
            String ytDlpPath = ytDlpExe.getAbsolutePath();  // 실제 경로로 사용

            if (new File(ytDlpPath).exists()) {
                // yt-dlp 경로가 존재하는 경우, 명령어 설정
                String command = ytDlpPath + " -f " + formatOption + " -o \"" + downloadLocation + "\" " + videoUrl;

                // ProcessBuilder로 yt-dlp 실행
                ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
                processBuilder.inheritIO();  // 콘솔 출력 연결 (실행 결과를 콘솔에 출력)

                Process process = processBuilder.start(); // 프로세스 시작

                // 프로세스가 완료될 때까지 기다립니다.
                int exitCode = process.waitFor();
                if (exitCode == 0) {
                    SwingUtilities.invokeLater(() -> {
                        resultLabel.setText("다운로드 완료!");
                        downloadButton.setText("다운로드"); // 완료 후 버튼 텍스트 초기화

                        // 다운로드 완료 후 URL 입력 필드를 초기화
                        urlField.setText(""); // 링크 입력 필드를 초기화
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        resultLabel.setText("다운로드 중 오류 발생!");
                        downloadButton.setText("다운로드");
                    });
                }
            } else {
                SwingUtilities.invokeLater(() -> {
                    resultLabel.setText("yt-dlp.exe 파일을 찾을 수 없습니다.");
                    downloadButton.setText("다운로드");
                });
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                resultLabel.setText("다운로드 중 오류 발생!");
                downloadButton.setText("다운로드");
            });
        }
    }
}
