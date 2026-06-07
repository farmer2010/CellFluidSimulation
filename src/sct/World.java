package sct;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class World extends JPanel{
	Random rand = new Random();
	Timer timer;
	int draw_type = 0;
	int mouse = 0;
	int mouse_obj = 0;
	boolean pause = false;
	int[] mouse_pos = new int[2];//0 - газ, 1 - твердое
	//
	double[][][] w = new double[3][Constant.world_scale[0]][Constant.world_scale[1]];
	//
	JButton stop_button = new JButton("Stop");
	JButton select_button = new JButton("Select");
	JButton set_button = new JButton("Set");
	JButton remove_button = new JButton("Remove");
	JButton gas_button = new JButton("Gas");
	JButton stone_button = new JButton("Stone");
	JButton source_button = new JButton("Gas source");
	JSlider concentration_slider = new JSlider(0, 10000, 1000);
	JSlider brush_slider = new JSlider(1, 15, 1);
	JButton clear_button = new JButton("Clear world");
	JButton clear_gas_button = new JButton("Clear gas");
	JButton stand_dr_button = new JButton("Standart");
	JButton gas_dr_button = new JButton("Gas");
	JButton stone_dr_button = new JButton("Stone");
	JButton source_dr_button = new JButton("Source");
	//
	Font font = new Font("arial", Font.BOLD, 18);
	//
	public World(){
		setLayout(null);
		timer = new Timer(10, new Listener());
		setBackground(new Color(90, 90, 90));
		addMouseListener(new Listener());
		addMouseMotionListener(new Listener());
		//
		stop_button.addActionListener(e -> start_stop());
		stop_button.setBounds(Constant.W - 300, 0, 250, 35);
		stop_button.setEnabled(true);
		add(stop_button);
        //
		JLabel mouse_label = new JLabel("Mouse function:");
		mouse_label.setBounds(Constant.W - 300, 35, 300, 20);
		mouse_label.setFont(font);
        add(mouse_label);
        //
        select_button.addActionListener(e -> change_mouse(0));
        select_button.setBounds(Constant.W - 300, 55, 95, 20);
        select_button.setEnabled(true);
		add(select_button);
		//
		set_button.addActionListener(e -> change_mouse(1));
        set_button.setBounds(Constant.W - 200, 55, 95, 20);
        set_button.setEnabled(true);
		add(set_button);
		//
		remove_button.addActionListener(e -> change_mouse(2));
		remove_button.setBounds(Constant.W - 100, 55, 95, 20);
		remove_button.setEnabled(true);
		add(remove_button);
		//
		JLabel layer_label = new JLabel("Set object:");
		layer_label.setBounds(Constant.W - 300, 75, 300, 20);
		layer_label.setFont(font);
        add(layer_label);
        //
        gas_button.addActionListener(e -> change_mouse_obj(0));
        gas_button.setBounds(Constant.W - 300, 95, 95, 20);
        gas_button.setEnabled(true);
		add(gas_button);
		//
		stone_button.addActionListener(e -> change_mouse_obj(1));
		stone_button.setBounds(Constant.W - 200, 95, 95, 20);
		stone_button.setEnabled(true);
		add(stone_button);
		//
		source_button.addActionListener(e -> change_mouse_obj(2));
		source_button.setBounds(Constant.W - 100, 95, 95, 20);
		source_button.setEnabled(true);
		add(source_button);
		//
		JLabel conc_label = new JLabel("Concentration:");
		conc_label.setBounds(Constant.W - 300, 115, 300, 20);
		conc_label.setFont(font);
        add(conc_label);
        //
        concentration_slider.setBounds(Constant.W - 300, 135, 250, 40);
        concentration_slider.setPaintLabels(true);
        concentration_slider.setMajorTickSpacing(1500);
		add(concentration_slider);
		//
		JLabel brush_label = new JLabel("Brush size:");
		brush_label.setBounds(Constant.W - 300, 175, 300, 20);
		brush_label.setFont(font);
        add(brush_label);
        //
        brush_slider.setBounds(Constant.W - 300, 195, 250, 40);
        brush_slider.setPaintLabels(true);
        brush_slider.setMajorTickSpacing(1);
		add(brush_slider);
		//
		JLabel cont_label = new JLabel("Controls:");
		cont_label.setBounds(Constant.W - 300, 235, 300, 20);
		cont_label.setFont(font);
        add(cont_label);
        //
        clear_button.addActionListener(e -> clear_world());
        clear_button.setBounds(Constant.W - 300, 255, 125, 20);
        clear_button.setEnabled(true);
		add(clear_button);
		//
		clear_gas_button.addActionListener(e -> clear_gas());
		clear_gas_button.setBounds(Constant.W - 170, 255, 125, 20);
		clear_gas_button.setEnabled(true);
		add(clear_gas_button);
		//
		JLabel draw_label = new JLabel("Draw type:");
		draw_label.setBounds(Constant.W - 300, 275, 300, 20);
		draw_label.setFont(font);
        add(draw_label);
        //
        stand_dr_button.addActionListener(e -> change_draw_type(0));
        stand_dr_button.setBounds(Constant.W - 300, 295, 125, 20);
        stand_dr_button.setEnabled(true);
		add(stand_dr_button);
		//
		gas_dr_button.addActionListener(e -> change_draw_type(1));
		gas_dr_button.setBounds(Constant.W - 170, 295, 125, 20);
		gas_dr_button.setEnabled(true);
		add(gas_dr_button);
		//
		stone_dr_button.addActionListener(e -> change_draw_type(2));
		stone_dr_button.setBounds(Constant.W - 300, 320, 125, 20);
		stone_dr_button.setEnabled(true);
		add(stone_dr_button);
		//
		source_dr_button.addActionListener(e -> change_draw_type(3));
		source_dr_button.setBounds(Constant.W - 170, 320, 125, 20);
		source_dr_button.setEnabled(true);
		add(source_dr_button);
		//
		timer.start();
	}
	public void paintComponent(Graphics canvas) {
		super.paintComponent(canvas);
		canvas.setColor(new Color(255, 255, 255));
		canvas.fillRect(0, 0, Constant.world_scale[0] * Constant.scale, Constant.world_scale[1] * Constant.scale);
		//
		for (int x = 0; x < Constant.world_scale[0]; x++) {
			for (int y = 0; y < Constant.world_scale[1]; y++) {
				if (draw_type == 0) {//смешанный
					if (w[2][x][y] > 0) {
						canvas.setColor(Constant.gradient(new Color(100, 0, 255), new Color(255, 0, 255), w[2][x][y] / 5000.0));
					}else {
						canvas.setColor(Constant.gradient(new Color(255, 255, 255), new Color(0, 0, 255), w[0][x][y] / 5000.0));
					}
				}else if (draw_type == 1) {//газ
					canvas.setColor(Constant.gradient(new Color(255, 255, 255), new Color(0, 0, 255), w[0][x][y] / 5000.0));
				}else if (draw_type == 2) {//камень
					canvas.setColor(Constant.gradient(new Color(255, 255, 255), new Color(143, 93, 54), w[1][x][y] / 5000.0));
				}else if (draw_type == 3) {//источники
					canvas.setColor(Constant.gradient(new Color(255, 255, 255), new Color(0, 0, 255), w[2][x][y] / 5000.0));
				}
				canvas.fillRect(x * Constant.scale, y * Constant.scale, Constant.scale, Constant.scale);
			}
		}
		if (mouse == 0) {//выбрать
			canvas.setColor(new Color(255, 200, 0, 128));
		}else if (mouse == 1) {//поставить
			canvas.setColor(new Color(0, 255, 0, 128));
		}else if (mouse == 2) {//сломать
			canvas.setColor(new Color(255, 0, 0, 128));
		}
		int brush = brush_slider.getValue();
		if (mouse != 0) {
			if (mouse_pos[0] - brush / 2 < Constant.world_scale[0]) {
				canvas.fillRect((mouse_pos[0] - brush / 2) * Constant.scale, (mouse_pos[1] - brush / 2) * Constant.scale, (brush + Math.min(0, Constant.world_scale[0] - (mouse_pos[0] - brush / 2 + brush))) * Constant.scale, brush * Constant.scale);
			}
		}else {
			if (mouse_pos[0] < Constant.world_scale[0]) {
				canvas.fillRect(mouse_pos[0] * Constant.scale, mouse_pos[1] * Constant.scale, Constant.scale, Constant.scale);
			}
		}
	}
	private class Listener extends MouseAdapter implements ActionListener{
		public void mousePressed(MouseEvent e) {
			if (e.getX() < Constant.W - 300 && e.getX() < Constant.world_scale[0] * Constant.scale && e.getY() < Constant.world_scale[1] * Constant.scale) {
				mouse_pos[0] = e.getX() / Constant.scale;
				mouse_pos[1] = e.getY() / Constant.scale;
				mouse_function();
			}
		}
		public void mouseDragged(MouseEvent e) {
			if (e.getX() < Constant.W - 300 && e.getX() < Constant.world_scale[0] * Constant.scale && e.getY() < Constant.world_scale[1] * Constant.scale) {
				mouse_pos[0] = e.getX() / Constant.scale;
				mouse_pos[1] = e.getY() / Constant.scale;
				mouse_function();
			}
		}
		public void mouseMoved(MouseEvent e) {
			mouse_pos[0] = e.getX() / Constant.scale;
			mouse_pos[1] = e.getY() / Constant.scale;
		}
		public void actionPerformed(ActionEvent e) {
			if (!pause) {
				gas(w[0]);
				//
				for (int x = 0; x < Constant.world_scale[0]; x++) {
					for (int y = 0; y < Constant.world_scale[1]; y++) {
						if (w[2][x][y] > 0){
							w[0][x][y] = w[2][x][y];
						}
					}
				}
			}
			repaint();
		}
	}
	public void mouse_function() {
		int brush = brush_slider.getValue();
		if (mouse == 0) {//выбрать
			//code will be here
		}else {
			for (int x = 0; x < brush; x++) {
				for (int y = 0; y < brush; y++) {
					int[] pos = {mouse_pos[0] + x - brush / 2, mouse_pos[1] + y - brush / 2};
					if (pos[0] >= 0 && pos[0] < Constant.world_scale[0] && pos[1] >= 0 && pos[1] < Constant.world_scale[1]) {
						if (mouse == 1) {
							w[mouse_obj][pos[0]][pos[1]] = concentration_slider.getValue();
						}else if (mouse == 2) {
							w[mouse_obj][pos[0]][pos[1]] = 0;
						}
					}
				}
			}
		}
	}
	//
	//СИМУЛЯЦИЯ
	//
	public static void gas(double[][] gas_map) {//распространение газа
		double[][] new_map = new double[Constant.world_scale[0]][Constant.world_scale[1]];
		for (int x = 0; x < Constant.world_scale[0]; x++) {
			for (int y = 0; y < Constant.world_scale[1]; y++) {
				gas_map[x][y] -= gas_map[x][y] * Constant.evaporation;//испарение
				double g = gas_map[x][y] * Constant.visco;
				double ox = g / 9;
				new_map[x][y] += gas_map[x][y] - g + ox;
				int count = 0;
				for (int i = 0; i < 8; i++) {
					int[] f = {x, y};
					int[] pos = Constant.get_rotate_position(i, f);
					if (pos[1] >= 0 && pos[1] < Constant.world_scale[1]) {
						new_map[pos[0]][pos[1]] += ox;
					}else {
						count++;
					}
				}
				for (int i = 0; i < count; i++) {
					new_map[x][y] += ox;
				}
			}
		}
		for (int x = 0; x < Constant.world_scale[0]; x++) {
			for (int y = 0; y < Constant.world_scale[1]; y++) {
				gas_map[x][y] = new_map[x][y];
			}
		}
	}
	//
	//КНОПКИ
	//
	public void start_stop() {
		pause = !pause;
		if (pause) {
			stop_button.setText("Start");
		}else {
			stop_button.setText("Stop");
		}
	}
	public void change_mouse(int num) {
		mouse = num;
	}
	public void change_mouse_obj(int num) {
		mouse_obj = num;
	}
	public void clear_world() {
		w = new double[3][Constant.world_scale[0]][Constant.world_scale[1]];
	}
	public void clear_gas() {
		for (int x = 0; x < Constant.world_scale[0]; x++) {
			for (int y = 0; y < Constant.world_scale[1]; y++) {
				w[0][x][y] = 0;
			}
		}
	}
	public void change_draw_type(int num) {
		draw_type = num;
	}
}
