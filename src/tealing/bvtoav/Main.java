package tealing.bvtoav;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
		}
		JFrame frame = new JFrame("Bilibili BV转AV工具");
		JTextField bv = new JTextField();
		JTextField av = new JTextField();
		JLabel bvLabel = new JLabel("BV");
		JLabel avLabel = new JLabel("AV");
		JLabel info = new JLabel("<html><body>茶凌儿@bilibili YilTeaLing@github<br>软件版本：0.0.3 引用库：gson-2.8.6<br>此软件完全免费，如您在获取此软件时付费则为诈骗<br>数据仅由系统生成，由用户操作不当导致的一切后果与原作者无关");
		JLabel titleLel = new JLabel();
		JButton getAVBtn = new JButton("BV转AV");
		JButton getBVBtn = new JButton("AV转BV");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setSize(310, 210);

		bvLabel.setBounds(10, 10, 20, 20);
		bv.setBounds(30, 10, 100, 20);
		avLabel.setBounds(10, 40, 20, 20);
		av.setBounds(30, 40, 100, 20);
		getAVBtn.setBounds(140, 10, 70, 50);
		getBVBtn.setBounds(215, 10, 70, 50);
		info.setBounds(10, 70, 280, 110);
		titleLel.setBounds(10, 70, 280, 20);
		

		getAVBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JsonElement json = getData(bv.getText(), true);
				if (json.getAsJsonObject().get("code").getAsInt() == 0) {
					av.setText(json.getAsJsonObject().get("data").getAsJsonObject().get("aid").getAsString());
					titleLel.setText(json.getAsJsonObject().get("data").getAsJsonObject().get("title").getAsString());
				}
				else {
					titleLel.setText("请求错误");
				}
			}
		});
		
		getBVBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JsonElement json = getData(av.getText(), false);
				if (json.getAsJsonObject().get("code").getAsInt() == 0) {
					bv.setText(json.getAsJsonObject().get("data").getAsJsonObject().get("bvid").getAsString());
					titleLel.setText(json.getAsJsonObject().get("data").getAsJsonObject().get("title").getAsString());
				}
				else {
					titleLel.setText("请求错误");
				}

			}
		});

		frame.add(bv);
		frame.add(av);
		frame.add(bvLabel);
		frame.add(avLabel);
		frame.add(getAVBtn);
		frame.add(getBVBtn);
		frame.add(info);
		frame.add(titleLel);

		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static JsonElement getData(String id, boolean isBV) {
		try {
			URL url;
			if (isBV) {
				url = new URL("https://api.bilibili.com/x/web-interface/view?bvid=" + id);
			}
			else {
				url = new URL("https://api.bilibili.com/x/web-interface/view?aid=" + id);
			}
			HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
			InputStream is = httpUrl.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				line = line.replaceAll("</?a[^>]*>", "");
				line = line.replaceAll("<(\\w+)[^>]*>", "<$1>");
				sb.append(line);
			}
			System.out.println(sb.toString());
			is.close();
			br.close();
			JsonElement json = new JsonParser().parseString(sb.toString());
			return json;
		} catch (Exception e) {
			//JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
			System.out.println(e.getClass().getName() + ": " + e.getMessage());
			return null;
		}
	}
}
