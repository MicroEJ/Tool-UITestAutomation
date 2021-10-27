/*
 * Java
 *
 * Copyright 2021 MicroEJ Corp. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
 */
package ej.fp.widget;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.Thread.State;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.border.Border;

import ej.fp.Device;
import ej.fp.widget.Pointer.PointerListener;
import ej.fp.widget.command.Command;
import ej.fp.widget.command.CommandBuilder;
import ej.fp.widget.command.CommandExecutionException;
import ej.fp.widget.command.CompareScreenshotCommand;
import ej.fp.widget.command.CompareScreenshotException;
import ej.fp.widget.command.WaitCommand;
import ej.fp.widget.recorder.RecorderPointer;
import ej.fp.widget.util.FileHelper;

/**
 * This is the Test Automation Tool core class, responsible for UI and it's features.
 */
public class Menu {

	private boolean recorderState = false;

	private final JFrame actualMenu;

	private final ActivityLogger activityLogger;

	private static final Logger LOGGER = Logger.getLogger(Menu.class.getName());

	private final JButton recordButton;
	private final JButton screenshotButton;
	private final JButton playButton;
	private final JProgressBar progressBar;
	private int itemCount;

	private final JComboBox<String> scenarioList;

	private final Icon playIcon;
	private final Icon recordIcon;
	private final Icon stopIcon;
	private final Icon screenshotIcon;
	private java.awt.Image favIcon;
	private final Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);

	private static final Object LOCK = new Object();

	/**
	 * Folder to save the tests files.
	 */
	private String folder = Paths.get(System.getProperty("user.home"), ".microej", "frontPanelRecorder") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			.toAbsolutePath().toString();

	private static final Menu INSTANCE = new Menu();

	/**
	 * Create a singleton version of Menu.
	 *
	 * @return the instance
	 */
	public static Menu getInstance() {
		return INSTANCE;
	}

	/**
	 * Opens the Menu view.
	 *
	 */
	private Menu() {
		this.playIcon = new ImageIcon(Menu.class.getResource("/icons/play.png")); //$NON-NLS-1$
		this.recordIcon = new ImageIcon(Menu.class.getResource("/icons/record.png")); //$NON-NLS-1$
		this.stopIcon = new ImageIcon(Menu.class.getResource("/icons/stop.png")); //$NON-NLS-1$
		this.screenshotIcon = new ImageIcon(Menu.class.getResource("/icons/screenshot.png")); //$NON-NLS-1$

		try {
			this.favIcon = ImageIO.read(Menu.class.getResource("/icons/favicon.png")); //$NON-NLS-1$
		} catch (IOException | IllegalArgumentException e) {
			Logger.getAnonymousLogger().warning(Messages.getString(Messages.ICON_LOADING_FAILED));
		}

		this.actualMenu = new JFrame();
		this.actualMenu.setTitle(Messages.getString(Messages.RECORDER));
		this.actualMenu.setIconImage(this.favIcon);
		this.actualMenu.setLocationRelativeTo(null);
		this.actualMenu.setVisible(true);

		JPanel pane = new JPanel();
		pane.setBackground(new Color(225, 225, 225));
		pane.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.actualMenu.setContentPane(pane);

		String[] fileList = getFolderFileList();
		this.itemCount = fileList.length;
		this.scenarioList = new JComboBox<>(fileList);
		this.scenarioList.setBounds(10, 105, 291, 22);

		JTextPane textPane = new JTextPane();
		this.activityLogger = new ActivityLogger(textPane);
		textPane.setBackground(Color.LIGHT_GRAY);
		textPane.setEditable(false);
		textPane.setBounds(10, 179, 291, 98);
		pane.add(textPane);

		this.playButton = createButton(this.playIcon);
		this.playButton.setLocation(47, 11);
		final Runnable playRunnable = new Runnable() {
			@Override
			public void run() {
				Menu.this.recordButton.setEnabled(false);
				Menu.this.screenshotButton.setEnabled(false);
				try {
					Menu.this.playFromFile(FileHelper.getSubfolder());
					Menu.this.activityLogger.push(Messages.getString(Messages.PLAYING_FINISHED));
				} catch (CompareScreenshotException e) {
					// Test ran succesfully, but failed. We can safely ignore this exception.
					LOGGER.log(Level.INFO, e.getMessage(), e);
				} catch (CommandExecutionException e) {
					LOGGER.log(Level.SEVERE, e.getMessage(), e);
					JOptionPane.showMessageDialog(null, Messages.getString(Messages.SCENARIOS_EXECUTION_FAILED));
				} finally {
					Menu.this.recordButton.setEnabled(true);
					Menu.this.screenshotButton.setEnabled(true);
					Menu.this.progressBar.setValue(0);
				}
			}
		};

		this.playButton.addActionListener(new ActionListener() {
			private Thread playThread = new Thread(playRunnable);

			@Override
			public void actionPerformed(ActionEvent e) {
				Menu.this.activityLogger.push(Messages.getString(Messages.PLAYING_STARTED));
				FileHelper.setSubfolder((String) Menu.this.scenarioList.getSelectedItem());
				FileHelper.createNewReportFile();
				try {
					if (this.playThread.getState() == State.TERMINATED) {
						this.playThread = new Thread(playRunnable);
					}
					if (!this.playThread.isAlive()) {
						this.playThread.start();
					}
				} catch (IllegalThreadStateException e1) {
					// The thread is already taken care of and this code should never happen.
					Logger.getAnonymousLogger().warning(e1.getMessage());
				}

			}
		});
		pane.setLayout(null);
		pane.add(this.playButton);

		this.recordButton = createButton(Menu.this.recordIcon);
		this.recordButton.setLocation(121, 11);
		this.recordButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				toggleRecordState();
			}
		});
		pane.add(this.recordButton);

		this.screenshotButton = createButton(this.screenshotIcon);
		this.screenshotButton.setLocation(195, 11);
		this.screenshotButton.setEnabled(false);
		this.screenshotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Menu.this.takeScreenshot();
			}
		});
		pane.add(this.screenshotButton);

		JLabel lblScenario = new JLabel(Messages.getString(Messages.SCENARIO_LIST));
		lblScenario.setBounds(10, 86, 64, 14);
		pane.add(lblScenario);
		if (fileList.length == 0) {
			this.playButton.setEnabled(false);
		}
		pane.add(this.scenarioList);

		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(10, 132, 291, 22);
		pane.add(this.progressBar);

		JLabel lblActionReport = new JLabel(Messages.getString(Messages.ACTION_REPORT));
		lblActionReport.setBounds(10, 157, 101, 14);
		pane.add(lblActionReport);
		this.actualMenu.setSize(327, 327);
		this.actualMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private JButton createButton(Icon icon) {
		JButton button = new JButton(icon);
		button.setBackground(Color.WHITE);
		button.setBorder(this.emptyBorder);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setSize(64, 64);
		return button;
	}

	/**
	 * Plays the scenario stored in a file.
	 *
	 * @param subfolderName
	 *            name of the file to be played.
	 * @throws CommandExecutionException
	 *             when executing {@code CompareScreenshotCommand} and it fails the test.
	 */
	public void playFromFile(String subfolderName) throws CommandExecutionException {
		synchronized (LOCK) {
			playFromFileInternal(subfolderName);
		}
	}

	private void playFromFileInternal(String subfolderName) throws CommandExecutionException {
		if (this.recorderState) {
			return;
		}
		FileHelper.setSubfolder(subfolderName);
		FileHelper.createNewReportFile();

		RecorderPointer pointer = Device.getDevice().getWidget(RecorderPointer.class);
		PointerListener listener = pointer.getListener();
		if (listener != null) {
			File file = Paths.get(getFolder(), subfolderName, "scenario.steps").toFile(); //$NON-NLS-1$
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

				ArrayList<Command<?>> c = new ArrayList<>();

				String sentence;
				while ((sentence = bufferedReader.readLine()) != null) {
					c.add(CommandBuilder.parse(sentence));
				}
				int arraySize = c.size();
				int index = 0;
				if (this.progressBar != null) {
					this.progressBar.setMinimum(0);
					this.progressBar.setMaximum(arraySize - 1);
				}
				for (Command<?> command : c) {
					command.execute();

					if (this.progressBar != null) {
						this.progressBar.setValue(index++);
					}
				}
			} catch (IOException e) {
				throw new CommandExecutionException(Messages.getString(Messages.READ_SCENARIO_FILE_ERROR), e);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new CommandExecutionException(e);
			}
		}
	}

	/**
	 * Saves the screen in a .raw file under the subfolder used for the scenario being recorded.
	 */
	public void takeScreenshot() {
		this.activityLogger.push(Messages.getString(Messages.SCREENSHOT_TAKEN));
		if (this.recorderState) {
			Display display = Device.getDevice().getWidget(Display.class);
			ej.fp.Image displayBuffer = display.visibleBuffer;
			String timestamp = LocalDateTime.now().toString();
			timestamp = timestamp.replace("-", "").replace("T", "").replace(":", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			timestamp = timestamp.substring(0, timestamp.indexOf(".")); //$NON-NLS-1$

			File screenshotFile = Paths.get(getFolder(), FileHelper.getSubfolder(), "screenshot" + timestamp + ".raw") //$NON-NLS-1$ //$NON-NLS-2$
					.toFile();
			try (FileOutputStream fileOutputStream = new FileOutputStream(screenshotFile)) {
				int[] pixels = new int[displayBuffer.getWidth() * displayBuffer.getHeight()];
				displayBuffer.getPixels(pixels);
				byte[] buffer = new byte[pixels.length * 4];
				for (int j = 0, k = 0; j < pixels.length; j++, k += 4) {
					buffer[k] = (byte) (pixels[j] >> 24); // A
					buffer[k + 1] = (byte) (pixels[j] >> 16); // R
					buffer[k + 2] = (byte) (pixels[j] >> 8); // G
					buffer[k + 3] = (byte) pixels[j]; // B
				}
				fileOutputStream.write(buffer);
				saveCommand(CompareScreenshotCommand.with(timestamp));
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				JOptionPane.showMessageDialog(null, Messages.getString(Messages.SAVE_FILE_ERROR));
				toggleRecordState();
			}
		}
	}

	/**
	 * Saves the command on the current scenario being recorded. This requires the steps file to have been created
	 * already.
	 *
	 * @param command
	 *            the command to be written
	 */
	public void saveCommand(Command<?> command) {
		if (this.recorderState) {
			String commandToBeSaved = CommandBuilder.serialize(command);
			String serializedCommand = CommandBuilder.serialize(new WaitCommand()) + System.lineSeparator()
					+ commandToBeSaved;
			File file = Paths.get(this.folder, FileHelper.getSubfolder(), "scenario.steps").toFile(); //$NON-NLS-1$
			try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true))) {
				dos.writeBytes(serializedCommand + System.lineSeparator());
				if (this.activityLogger != null) {
					this.activityLogger.push(serializedCommand.split(",")[0]); //$NON-NLS-1$
					this.activityLogger.push(commandToBeSaved.split(",")[0]); //$NON-NLS-1$
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				JOptionPane.showMessageDialog(null, Messages.getString(Messages.SAVE_FILE_ERROR));
				toggleRecordState();
			}
		}
	}

	/**
	 * The folder where all the files are stored.
	 *
	 * @return the folder where the data will be saved
	 */
	public String getFolder() {
		return this.folder;
	}

	/**
	 * Sets the folder where all the files will be stored.
	 *
	 * @param folder
	 *            the folder where the data will be saved
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Gets all the scenarios files under the {@code folder}.
	 *
	 * @return an array with all the scenarios file names.
	 */
	private String[] getFolderFileList() {
		String[] folderList = new File(this.folder).list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});

		return folderList == null ? new String[] {} : folderList;
	}

	/**
	 * Toggles recording state
	 */
	public void toggleRecordState() {
		try {
			if (Menu.this.recorderState) {
				Menu.this.activityLogger.push(Messages.getString(Messages.RECORDING_STOPPED));
				Menu.this.recordButton.setIcon(Menu.this.recordIcon);
				String[] fileList = getFolderFileList();
				if (Menu.this.itemCount != fileList.length) {
					Menu.this.itemCount = fileList.length;
					Menu.this.scenarioList.setModel(new DefaultComboBoxModel<>(fileList));
				}
				Menu.this.screenshotButton.setEnabled(false);
				Menu.this.playButton.setEnabled(true);
			} else {
				WaitCommand.updateLastEventTime();
				FileHelper.createNewCommandFile();
				Menu.this.recordButton.setIcon(Menu.this.stopIcon);
				Menu.this.activityLogger.push(Messages.getString(Messages.RECORDING_STARTED));
				Menu.this.screenshotButton.setEnabled(true);
				Menu.this.playButton.setEnabled(false);
			}
			Menu.this.recorderState = !Menu.this.recorderState;
		} catch (IOException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(null, Messages.getString(Messages.SAVE_FILE_ERROR));
		}
	}
}
