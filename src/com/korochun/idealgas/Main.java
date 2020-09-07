package com.korochun.idealgas;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
	
	private static boolean movePiston = false, run = true, showVectors = false;
	private static int bigParticles = 5, particles = 1000, pistonLimit = 160;
	private static double particleMass = 4, particleSpeed = 3, vectorMulti = 5, pistonSpeed = 1;
	private static JButton move;
	
	static boolean isMovePiston() {
		return movePiston;
	}
	static boolean isRun() {
		return run;
	}
	static boolean isShowVectors() {
		return showVectors;
	}
	static int getBigParticleCount() {
		return bigParticles;
	}
	static int getParticleCount() {
		return particles;
	}
	static double getParticleMass() {
		return particleMass;
	}
	static double getParticleSpeed() {
		return particleSpeed;
	}
	static int getPistonLimit() {
		return pistonLimit;
	}
	static double getPistonSpeed() {
		return pistonSpeed;
	}
	static double getVectorMulti() {
		return vectorMulti;
	}
	static void setPistonMove(boolean b) {
		movePiston = b;
		if (movePiston) {
			move.setText("Stop");
		} else {
			move.setText("Move");
		}
	}
	
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) { }
		
		JFrame frame = new JFrame("2D Ideal Gas");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(200, 200);

		Dimension yFiller = new Dimension(280, 10);
		Dimension size = new Dimension(280, 350);
		
		Box options = new Box(BoxLayout.Y_AXIS);
		options.setMaximumSize(size);
		options.setMinimumSize(size);
		options.setPreferredSize(size);
		
		JLabel opt = new JLabel("Options");
		opt.setFont(opt.getFont().deriveFont(Font.BOLD));
		opt.setAlignmentX(Component.CENTER_ALIGNMENT);
		options.add(opt);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box countBox = new Box(BoxLayout.X_AXIS);
		
		JLabel countLabel = new JLabel("Particle Count", SwingConstants.LEFT);
		countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		countBox.add(countLabel);
		
		JSlider countSlider = new JSlider(0, 2500, 1250);
		countSlider.setPaintTicks(true);
		countSlider.setMajorTickSpacing(625);
		countSlider.setMinorTickSpacing(125);
		countSlider.setSnapToTicks(true);
		countSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				particles = countSlider.getValue();
			}
		});
		countBox.add(countSlider);
		
		options.add(countBox);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box bigCountBox = new Box(BoxLayout.X_AXIS);
		
		JLabel bigCountLabel = new JLabel("Big Particle Count", SwingConstants.LEFT);
		bigCountLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		bigCountBox.add(bigCountLabel);
		
		JSlider bigCountSlider = new JSlider(0, 20, 5);
		bigCountSlider.setPaintTicks(true);
		bigCountSlider.setMajorTickSpacing(5);
		bigCountSlider.setMinorTickSpacing(1);
		bigCountSlider.setSnapToTicks(true);
		bigCountSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				bigParticles = bigCountSlider.getValue();
			}
		});
		bigCountBox.add(bigCountSlider);
		
		options.add(bigCountBox);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box massBox = new Box(BoxLayout.X_AXIS);
		
		JLabel massLabel = new JLabel("Particle Mass", SwingConstants.LEFT);
		massLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		massBox.add(massLabel);
		
		JSlider massSlider = new JSlider(0, 20, 4);
		massSlider.setPaintTicks(true);
		massSlider.setMajorTickSpacing(5);
		massSlider.setMinorTickSpacing(1);
		massSlider.setSnapToTicks(true);
		massSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				particleMass = massSlider.getValue();
			}
		});
		massBox.add(massSlider);
		
		options.add(massBox);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box speedBox = new Box(BoxLayout.X_AXIS);
		
		JLabel speedLabel = new JLabel("Particle Speed", SwingConstants.LEFT);
		speedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		speedBox.add(speedLabel);
		
		JSlider speedSlider = new JSlider(0, 10, 3);
		speedSlider.setPaintTicks(true);
		speedSlider.setMajorTickSpacing(5);
		speedSlider.setMinorTickSpacing(1);
		speedSlider.setSnapToTicks(true);
		speedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				particleSpeed = speedSlider.getValue();
			}
		});
		speedBox.add(speedSlider);
		
		options.add(speedBox);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box vecBox = new Box(BoxLayout.X_AXIS);
		
		JLabel vecLabel = new JLabel("Vector Size", SwingConstants.LEFT);
		vecLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		vecBox.add(vecLabel);
		
		JSlider vecSlider = new JSlider(0, 10, 5);
		vecSlider.setEnabled(false);
		vecSlider.setPaintTicks(true);
		vecSlider.setMajorTickSpacing(5);
		vecSlider.setMinorTickSpacing(1);
		vecSlider.setSnapToTicks(true);
		vecSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				vectorMulti = vecSlider.getValue();
			}
		});
		vecBox.add(vecSlider);
		
		JCheckBox vec = new JCheckBox("Show Vectors");
		vec.setAlignmentX(Component.RIGHT_ALIGNMENT);
		vec.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				showVectors = vec.isSelected();
				vecSlider.setEnabled(showVectors);
			}
		});
		
		options.add(vec);
		options.add(Box.createRigidArea(yFiller));
		options.add(vecBox);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box buttons = new Box(BoxLayout.X_AXIS);
		
		JButton pause = new JButton("Pause");
		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (run) {
					run = false;
					pause.setText("Unpause");
				} else {
					run = true;
					pause.setText("Pause");
				}
			}
		});
		buttons.add(pause);
		
		JButton restart = new JButton("Restart");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Physics.instance.stop();
				Physics.instance.start();
			}
		});
		buttons.add(restart);
		
		options.add(buttons);

		size = new Dimension(300, 185);
		
		JScrollPane optionPane = new JScrollPane(options, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		optionPane.setMaximumSize(size);
		optionPane.setMinimumSize(size);
		optionPane.setPreferredSize(size);
		
		size = new Dimension(280, 350);
		
		Box piston = new Box(BoxLayout.Y_AXIS);
		piston.setMaximumSize(size);
		piston.setMinimumSize(size);
		piston.setPreferredSize(size);
		
		piston.add(Box.createRigidArea(yFiller));
		
		JLabel pist = new JLabel("Piston Control");
		pist.setFont(pist.getFont().deriveFont(Font.BOLD));
		pist.setAlignmentX(Component.CENTER_ALIGNMENT);
		piston.add(pist);
		
		options.add(Box.createRigidArea(yFiller));
		
		Box pistonSpeedBox = new Box(BoxLayout.X_AXIS);
		
		JLabel pistonSpeedLabel = new JLabel("Piston Speed", SwingConstants.LEFT);
		pistonSpeedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		pistonSpeedBox.add(pistonSpeedLabel);
		
		JSlider pistonSpeedSlider = new JSlider(0, 5, 1);
		pistonSpeedSlider.setPaintTicks(true);
		pistonSpeedSlider.setMajorTickSpacing(5);
		pistonSpeedSlider.setMinorTickSpacing(1);
		pistonSpeedSlider.setSnapToTicks(true);
		pistonSpeedSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pistonSpeed = pistonSpeedSlider.getValue();
			}
		});
		pistonSpeedBox.add(pistonSpeedSlider);
		
		piston.add(pistonSpeedBox);
		
		piston.add(Box.createRigidArea(yFiller));
		
		Box pistonLimitBox = new Box(BoxLayout.X_AXIS);
		
		JLabel pistonLimitLabel = new JLabel("Piston Limit", SwingConstants.LEFT);
		pistonLimitLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		pistonLimitBox.add(pistonLimitLabel);
		
		JSlider pistonLimitSlider = new JSlider(0, 240, 160);
		pistonLimitSlider.setInverted(true);
		pistonLimitSlider.setPaintTicks(true);
		pistonLimitSlider.setMajorTickSpacing(80);
		pistonLimitSlider.setMinorTickSpacing(10);
		pistonLimitSlider.setSnapToTicks(true);
		pistonLimitSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pistonLimit = pistonLimitSlider.getValue();
			}
		});
		pistonLimitBox.add(pistonLimitSlider);
		
		piston.add(pistonLimitBox);
		
		piston.add(Box.createRigidArea(yFiller));
		
		move = new JButton("Move");
		move.setAlignmentX(Component.CENTER_ALIGNMENT);
		move.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setPistonMove(!movePiston);
			}
		});
		piston.add(move);
		
		size = new Dimension(300, 185);
		
		JScrollPane pistonPane = new JScrollPane(piston, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		pistonPane.setMaximumSize(size);
		pistonPane.setMinimumSize(size);
		pistonPane.setPreferredSize(size);
		
		size = new Dimension(300, 480);
		
		JPanel menu = new JPanel();
		menu.setLayout(new BorderLayout());
		menu.setMaximumSize(size);
		menu.setMinimumSize(size);
		menu.setPreferredSize(size);
		menu.add(optionPane, BorderLayout.NORTH);
		menu.add(pistonPane, BorderLayout.CENTER);
		menu.add(Graph.instance, BorderLayout.SOUTH);
		
		frame.add(Physics.display, BorderLayout.WEST);
		frame.add(menu, BorderLayout.EAST);
		frame.setVisible(true);
		
		Container pane = frame.getContentPane();
		int width  = 840 - pane.getWidth(),
			height = 680 - pane.getHeight();
		Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		frame.setBounds((screen.width - width) / 2, (screen.height - height) / 2, width, height);
		
		Physics.instance.start();
		Graph.instance.start();
	}
}
