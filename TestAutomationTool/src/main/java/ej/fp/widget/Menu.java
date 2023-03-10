/*
 * Java
 *
 * Copyright 2021-2023 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.fp.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.Thread.State;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

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
import ej.fp.widget.util.ImageHelper;

/**
 * This is the Test Automation Tool core class, responsible for UI and it's features.
 */
public class Menu implements CaretListener {

	/** Dominant color used when auto color is selected. */
	public static final int AUTO_COLOR = -1;

	private static final Logger LOGGER = Logger.getLogger(Menu.class.getName());

	private static final int STOPPED = 0;
	private static final int PLAYING = 1;
	private static final int PAUSED = 2;
	private static final int RECORDING = 3;

	private static final String PATH_KEY = "mock.ej.library.test.path"; //$NON-NLS-1$
	private static final String ENTER_SCENARIO_NAME = "Enter scenario name:"; //$NON-NLS-1$
	private static final String ENTER_MASK_NAME = "Enter the name of mask"; //$NON-NLS-1$
	private static final String ENTER_MASK_NEW_NAME = "Enter the new name of mask"; //$NON-NLS-1$
	private static final String SCENARIO_ALREADY_EXISTS = "This scenario name already exists"; //$NON-NLS-1$
	private static final String ENTER_VALID_NAME = "Please enter a valid name"; //$NON-NLS-1$
	private static final String MASK_ENABLED = "Mask enabled"; //$NON-NLS-1$
	private static final String MASK_ENABLED_MSG = "'Enable mask' will be automatically unchecked after taking a screenshot."; //$NON-NLS-1$
	private static final String ALERT = "Alert"; //$NON-NLS-1$
	private static final String TMP_WITH_MASK = "tmp_with_mask";//$NON-NLS-1$
	private static final String PNG = "png";//$NON-NLS-1$
	private static final String FORMAT_NAME_PNG = "PNG";//$NON-NLS-1$
	private static final Object LOCK = new Object();

	private static final int PATH_TEXT_FIELD_HEIGHT = 20;
	private static final int NUMBER_COLUMNS = 4;
	private static final String STEPS_FILE_NAME = "scenario.steps"; //$NON-NLS-1$
	private static final String EMPTY_TEXT = ""; //$NON-NLS-1$
	private static final String COMMA = ","; //$NON-NLS-1$
	private static final String ZERO_TEXT = "0"; //$NON-NLS-1$
	private static final int HUNDRED_PERCENT = 100;
	private static final String AUTO = "AUTO"; //$NON-NLS-1$
	private static final String RED = "RED"; //$NON-NLS-1$
	private static final String GREEN = "GREEN";//$NON-NLS-1$
	private static final String BLUE = "BLUE";//$NON-NLS-1$
	private static final String PINK = "PINK";//$NON-NLS-1$
	private static final String WHITE = "WHITE";//$NON-NLS-1$
	private static final String BLACK = "BLACK";//$NON-NLS-1$
	private static final String YELLOW = "YELLOW";//$NON-NLS-1$
	private static final String ORANGE = "ORANGE";//$NON-NLS-1$
	private static final int WHITE_COLOR = 0xFFFFFF;

	private static final String OVERRIDE = "OVERRIDE";//$NON-NLS-1$
	private static final String PLAY = "PLAY";//$NON-NLS-1$
	private static final String START_RECORD = "START RECORDING";//$NON-NLS-1$
	private static final String TAKE_SCREENSHOT = "TAKE SCREENSHOT";//$NON-NLS-1$
	private static final String STOP_RECORD = "STOP RECORDING";//$NON-NLS-1$
	private static final String PAUSE = "PAUSE";//$NON-NLS-1$

	private final Border emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
	private final JFrame actualMenu;
	private final ActivityLogger activityLogger;
	private final JButton recordButton;
	private final JButton screenshotButton;
	private final JButton playButton;
	private final JButton overrideButton;
	private JProgressBar progressBar;
	private final Icon playIcon;

	private final Icon recordIcon;
	private final Icon stopIcon;
	private final Icon screenshotIcon;
	private final Icon overrideIcon;

	private final Icon pauseIcon;

	private JCheckBox enableMaskCheckbox;
	private JCheckBox showAllMaskCheckbox;
	private JFormattedTextField maskXTextField;
	private JFormattedTextField maskYTextField;
	private JFormattedTextField maskWidthTextField;
	private JFormattedTextField maskHeightTextField;
	private JComboBox<String> scenarioList;

	private JComboBox<String> colorsMaskList;

	private JLabel currentImage;
	private JLabel currentImageMask;
	private JLabel comparedImage;
	private JLabel comparePercentLabel;
	private JPanel mainScreenshotPanel;
	private JPanel maskShowMainPanel;
	private int mockWidth;
	private int maskContainerHeight;
	private int containerWidth;
	private int displayHeight;
	private int displayWidth;
	private java.awt.Image favIcon;
	private JTextField pathTextField;
	private JButton browseButton;
	private int itemCount;
	private int state = ActionState.IDLE;
	private int recorderState = STOPPED;
	private int playState = STOPPED;
	private int playingCommandsIndex = 0;
	long startPause;
	private long recordingStartPauseTime;
	private long recordingEndPauseTime;
	private boolean useLatestPauseTime = false;
	private JComboBox<Mask> cbListMasks;
	private JButton btnRename;
	private JButton btnAdd;
	private JButton btnDelete;

	private JLabel labelOverride;
	private JLabel labelPlay;
	private JLabel labelRecord;
	private JLabel labelTakeScreenshot;
	boolean firstTimeEnableMaskChecked = false;

	/**
	 * Folder to save the tests files.
	 */
	private String folder;

	private static final Menu INSTANCE = new Menu();

	/**
	 * Opens the Menu view.
	 */
	private Menu() {
		setupMockDimensions();
		setupPath();

		this.playIcon = new ImageIcon(Menu.class.getResource("/icons/play.png")); //$NON-NLS-1$
		this.recordIcon = new ImageIcon(Menu.class.getResource("/icons/record.png")); //$NON-NLS-1$
		this.stopIcon = new ImageIcon(Menu.class.getResource("/icons/stop.png")); //$NON-NLS-1$
		this.screenshotIcon = new ImageIcon(Menu.class.getResource("/icons/screenshot.png")); //$NON-NLS-1$
		this.overrideIcon = new ImageIcon(Menu.class.getResource("/icons/update.png")); //$NON-NLS-1$
		this.pauseIcon = new ImageIcon(Menu.class.getResource("/icons/pause.png")); //$NON-NLS-1$

		try {
			this.favIcon = ImageIO.read(Menu.class.getResource("/icons/favicon.png")); //$NON-NLS-1$
		} catch (IOException | IllegalArgumentException e) {
			String msg = Messages.getString(Messages.ICON_LOADING_FAILED) + " Exception : " + e; //$NON-NLS-1$
			LOGGER.log(Level.WARNING, msg, e);
		}

		this.actualMenu = new JFrame();
		this.actualMenu.setTitle(Messages.getString(Messages.RECORDER));
		this.actualMenu.setIconImage(this.favIcon);
		this.actualMenu.setLocationRelativeTo(null);
		this.actualMenu.setVisible(true);

		JPanel pane = new JPanel();

		pane.setBackground(AppColors.getLightGrey());
		pane.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.actualMenu.setContentPane(pane);

		pane.add(createTabbedPane());
		createOrUpdateScenarioList();
		JTextPane textPane = new JTextPane();
		this.activityLogger = new ActivityLogger(textPane);
		textPane.setBackground(Color.LIGHT_GRAY);

		textPane.setEditable(false);

		textPane.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.STATUS_REPORT_Y, this.containerWidth,
				MenuSizes.STATUS_REPORT_HEIGHT);

		// Add the text logger area
		pane.add(textPane);

		pane.setLayout(null);

		this.overrideButton = createButton(this.overrideIcon);

		this.playButton = createButton(this.playIcon);

		this.recordButton = createButton(Menu.this.recordIcon);

		this.screenshotButton = createButton(this.screenshotIcon);

		this.screenshotButton.setEnabled(false);

		JLabel lblActionReport = new JLabel(Messages.getString(Messages.ACTION_REPORT));

		lblActionReport.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.ACTION_REPORT_LABEL_Y, MenuSizes.ACTION_REPORT_WIDTH,
				MenuSizes.ACTION_REPORT_HEIGHT);

		pane.add(lblActionReport);
		// Add the progress
		pane.add(createProgressBarPanel());
		pane.add(createTableButtons());

		int mockHeight = MenuSizes.MOCK_BASE_HEIGHT + MenuSizes.SPACING_MOCK + Menu.this.maskContainerHeight;

		this.actualMenu.setSize(this.mockWidth, mockHeight);

		this.actualMenu.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.actualMenu.setLocation(0, 0);
		setupPlayActionListener();
		setupUpdateActionListener();
		setupRecordActionListeners();
		handleMaskDisplay(true);
	}

	private JPanel createTableButtons() {

		JPanel buttonsNames = new JPanel();
		JPanel labelsNames = new JPanel();
		JPanel vContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));

		vContainer.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		vContainer.setLayout(new BoxLayout(vContainer, BoxLayout.Y_AXIS));

		vContainer.setBounds(MenuSizes.MARGIN_LEFT, 0, MenuSizes.WIDTH_CONTAINER_BUTTON,
				MenuSizes.HEIGHT_CONTAINER_BUTTON);

		buttonsNames.setBackground(AppColors.getLightGrey());
		labelsNames.setBackground(AppColors.getLightGrey());

		buttonsNames.add(this.overrideButton);
		buttonsNames.add(this.playButton);
		buttonsNames.add(this.recordButton);
		buttonsNames.add(this.screenshotButton);

		this.labelOverride = new JLabel(OVERRIDE);
		this.labelOverride.setHorizontalAlignment(JLabel.CENTER);
		this.labelPlay = new JLabel(PLAY);
		this.labelPlay.setHorizontalAlignment(JLabel.CENTER);
		this.labelRecord = new JLabel(START_RECORD);
		this.labelRecord.setHorizontalAlignment(JLabel.CENTER);
		this.labelTakeScreenshot = new JLabel(TAKE_SCREENSHOT);
		this.labelTakeScreenshot.setHorizontalAlignment(JLabel.CENTER);

		labelsNames.add(this.labelOverride);
		labelsNames.add(this.labelPlay);
		labelsNames.add(this.labelRecord);
		labelsNames.add(this.labelTakeScreenshot);

		labelsNames.setLayout(new GridLayout(1, NUMBER_COLUMNS));

		buttonsNames.setLayout(new GridLayout(1, NUMBER_COLUMNS));

		buttonsNames.setSize(MenuSizes.WIDTH_GRIDLAYOUT_BUTTON, MenuSizes.HEIGHT_GRIDLAYOUT_BUTTON);
		buttonsNames.setVisible(true);

		labelsNames.setSize(MenuSizes.WIDTH_GRIDLAYOUT_LABEL, MenuSizes.HEIGHT_GRIDLAYOUT_LABEL);

		labelsNames.setVisible(true);

		vContainer.add(buttonsNames);
		vContainer.add(labelsNames);

		return vContainer;
	}

	/**
	 * Create a singleton version of Menu.
	 *
	 * @return the instance
	 */
	public static Menu getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets the start pause time when recording.
	 *
	 * @return the recording start pause time.
	 */
	public long getRecordingStartPauseTime() {
		return this.recordingStartPauseTime;
	}

	/**
	 * Sets the start pause time when recording.
	 *
	 * @param recordingStartPauseTime
	 *            the recording start pause time to set.
	 */
	public void setRecordingStartPauseTime(long recordingStartPauseTime) {
		this.recordingStartPauseTime = recordingStartPauseTime;
	}

	/**
	 * Gets the end pause time when recording.
	 *
	 * @return the recording end pause time.
	 */
	public long getRecordingEndPauseTime() {
		return this.recordingEndPauseTime;
	}

	/**
	 * Sets the end pause time when recording.
	 *
	 * @param recordingEndPauseTime
	 *            the recording end pause time to set.
	 */
	public void setRecordingEndPauseTime(long recordingEndPauseTime) {
		this.recordingEndPauseTime = recordingEndPauseTime;
	}

	/**
	 * Checks if latest pause time will be used.
	 *
	 * @return <code>true</code> if latest pause time is used otherwie return <code>false</code>.
	 */
	public boolean didUseLatestPauseTime() {
		return this.useLatestPauseTime;
	}

	/**
	 * Sets the latest pause time state.
	 *
	 * @param useLatestPauseTime
	 *            the latest pause time state to set.
	 */
	public void setUseLatestPauseTime(boolean useLatestPauseTime) {
		this.useLatestPauseTime = useLatestPauseTime;
	}

	private void renameInputDialog() {

		JFrame frame = new JFrame();

		String newNameScenario = JOptionPane.showInputDialog(frame, ENTER_SCENARIO_NAME,
				FileHelper.getDefaultScenarioName());

		String[] namesScenarios = getFolderFileList();
		if (newNameScenario != null) {
			if (!newNameScenario.isEmpty()) {
				for (int i = 0; i < namesScenarios.length; i++) {
					if (namesScenarios[i].equals(newNameScenario)) {
						JFrame frameAlert = new JFrame();
						JOptionPane.showMessageDialog(frameAlert, SCENARIO_ALREADY_EXISTS, ALERT,
								JOptionPane.WARNING_MESSAGE);
						break;

					}
				}
			}
			toggleRecordState(newNameScenario);
		}

	}

	private void renameScenario() {

		JFrame frame = new JFrame();

		String newNameScenario = JOptionPane.showInputDialog(frame, ENTER_SCENARIO_NAME);

		String[] namesScenarios = getFolderFileList();

		for (int i = 0; i < namesScenarios.length; i++) {
			if (namesScenarios[i].equals(newNameScenario)) {
				JFrame frameAlert = new JFrame();
				JOptionPane.showMessageDialog(frameAlert, SCENARIO_ALREADY_EXISTS, ALERT, JOptionPane.WARNING_MESSAGE);
				break;

			}
		}

		String nameSc = Menu.this.scenarioList.getSelectedItem().toString();
		Paths.get(getFolder(), nameSc);
		if (newNameScenario != null && !newNameScenario.isEmpty()) {
			try {
				Path source = Paths.get(getFolder(), nameSc);
				Files.move(source, source.resolveSibling(Paths.get(getFolder(), newNameScenario)));

				Menu.this.pathTextField.setText(getFolder());

				String[] fileList = getFolderFileList();

				Menu.this.itemCount = fileList.length;
				Menu.this.scenarioList.setModel(new DefaultComboBoxModel<>(fileList));

			} catch (Exception e) {
				LOGGER.log(Level.INFO, e.getMessage(), e);

			}
		}

	}

	private void setupPlayActionListener() {
		final Runnable playRunnable = new Runnable() {
			@Override
			public void run() {

				handlePlayRunnable();
			}
		};

		this.playButton.addActionListener(new ActionListener() {
			private Thread playThread = new Thread(playRunnable);

			@Override
			public void actionPerformed(ActionEvent e) {

				if (Menu.this.recorderState != RECORDING) {

					Menu.this.maskShowMainPanel.setVisible(true);

					Menu.this.mainScreenshotPanel.setVisible(true);
				}

				if (Menu.this.recorderState == STOPPED) {

					// In case we are playing scenario
					FileHelper.setSubfolder((String) Menu.this.scenarioList.getSelectedItem());
					FileHelper.createNewReportFile();

					try {
						if (this.playThread.getState() == State.TERMINATED) {
							this.playThread = new Thread(playRunnable);
						}
						if (!this.playThread.isAlive()) {
							if (Menu.this.playState == STOPPED) {
								Menu.this.playState = PLAYING;
								Menu.this.playingCommandsIndex = 0;

								// Init compare Screenshot zone to remove previous compared images.
								setCurrentImage(null);
								setComparedImage(null);
								setComparePercent(0);

							} else if (Menu.this.playState == PAUSED) {
								Menu.this.playState = PLAYING;

							}
							this.playThread.start();
							Menu.this.playButton.setIcon(Menu.this.pauseIcon);
							Menu.this.labelPlay.setText(PAUSE);
							Menu.this.activityLogger.push(Messages.getString(Messages.PLAYING_STARTED));
							return;
						}
					} catch (IllegalThreadStateException e1) {
						// The thread is already taken care of and this code should never happen.
						LOGGER.log(Level.INFO, e1.getMessage(), e1);
					}

					if (Menu.this.playState == PLAYING) {
						Menu.this.playState = PAUSED;
						Menu.this.playButton.setIcon(Menu.this.playIcon);

						Menu.this.labelPlay.setText(PLAY);

						Menu.this.activityLogger.push(Messages.getString(Messages.PAUSE_TIME));
					} else if (Menu.this.playState == PAUSED) {
						Menu.this.playState = PLAYING;
						Menu.this.playButton.setIcon(Menu.this.pauseIcon);
						Menu.this.labelPlay.setText(PAUSE);

						Menu.this.activityLogger.push(Messages.getString(Messages.RESUME_TIME));
					}
				} else {
					// In case we are recording scenario

					Menu.this.maskShowMainPanel.setVisible(true);
					Menu.this.mainScreenshotPanel.setVisible(true);
					updateDisplayedMask();

					if (Menu.this.recorderState == RECORDING) {
						setUseLatestPauseTime(true);
						setRecordingStartPauseTime(System.currentTimeMillis());
						Menu.this.recorderState = PAUSED;
						Menu.this.playButton.setIcon(Menu.this.playIcon);
						Menu.this.labelPlay.setText(PLAY);
					} else if (Menu.this.recorderState == PAUSED) {
						setRecordingEndPauseTime(System.currentTimeMillis());
						Menu.this.recorderState = RECORDING;
						Menu.this.playButton.setIcon(Menu.this.pauseIcon);
						Menu.this.labelPlay.setText(PAUSE);
					}
				}
			}
		});

	}

	private void handlePlayRunnable() {

		Menu.this.recordButton.setEnabled(false);
		Menu.this.screenshotButton.setEnabled(false);
		Menu.this.overrideButton.setEnabled(false);
		try {
			Menu.this.playFromFile(FileHelper.getSubfolder());

			if (Menu.this.playState == STOPPED) {
				Menu.this.activityLogger.push(Messages.getString(Messages.PLAYING_FINISHED));
			}
		} catch (CompareScreenshotException e) {
			// Test ran succesfully, but failed. We can safely ignore this exception.
			LOGGER.log(Level.INFO, e.getMessage(), e);
			Menu.this.activityLogger.push(Messages.getString(Messages.COMPARE_SCREENSHOT_EXCEPTION));
			Menu.this.activityLogger.push(Messages.getString(Messages.PLAYING_FINISHED));
		} catch (CommandExecutionException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(null, Messages.getString(Messages.SCENARIOS_EXECUTION_FAILED));
		} finally {
			if (Menu.this.playState == PLAYING) {
				Menu.this.playState = STOPPED;
			}
			if (Menu.this.playState == STOPPED) {
				resetButtonsAndState();
			}
		}
	}

	/**
	 * Resets the UI buttons and set the state to idle.
	 */
	public void resetButtonsAndState() {
		Menu.this.playButton.setIcon(Menu.this.playIcon);

		Menu.this.labelPlay.setText(PLAY);
		Menu.this.playButton.setEnabled(true);
		Menu.this.recordButton.setEnabled(true);
		Menu.this.screenshotButton.setEnabled(false);
		Menu.this.overrideButton.setEnabled(true);
		Menu.this.progressBar.setValue(0);
		// Add this line to re-init the state
		Menu.this.state = ActionState.IDLE;
		Menu.this.recorderState = STOPPED;
	}

	private void setupUpdateActionListener() {
		final Runnable updateRunnable = new Runnable() {
			@Override
			public void run() {
				handleUpdateRunnable();
			}
		};

		this.overrideButton.addActionListener(new ActionListener() {
			private Thread updateThread = new Thread(updateRunnable);

			@Override
			public void actionPerformed(ActionEvent e) {

				if (Menu.this.recorderState != RECORDING) {

					Menu.this.maskShowMainPanel.setVisible(true);
					Menu.this.mainScreenshotPanel.setVisible(true);
				}

				Menu.this.activityLogger.push(Messages.getString(Messages.UPDATE_STARTED));
				FileHelper.setSubfolder((String) Menu.this.scenarioList.getSelectedItem());
				FileHelper.createNewReportFile();
				try {
					if (this.updateThread.getState() == State.TERMINATED) {
						Menu.this.state = ActionState.IDLE;

						this.updateThread = new Thread(updateRunnable);
					}
					if (!this.updateThread.isAlive()) {
						Menu.this.state = ActionState.UPDATE;
						Menu.this.playingCommandsIndex = 0;
						// Init compare Screenshot zone to remove previous compared images.
						setCurrentImage(null);
						setComparedImage(null);
						setComparePercent(0);
						this.updateThread.start();
					}
				} catch (IllegalThreadStateException e1) {
					// The thread is already taken care of and this code should never happen.
					LOGGER.log(Level.SEVERE, e1.getMessage(), e1);
				}
			}
		});
	}

	private void handleUpdateRunnable() {
		Menu.this.playButton.setEnabled(false);
		Menu.this.recordButton.setEnabled(false);
		Menu.this.screenshotButton.setEnabled(false);
		try {
			Menu.this.updateFromFile(FileHelper.getSubfolder());
			Menu.this.activityLogger.push(Messages.getString(Messages.UPDATE_FINISHED));
		} catch (CompareScreenshotException e) {
			// Test ran succesfully, but failed. We can safely ignore this exception.
			LOGGER.log(Level.INFO, e.getMessage(), e);
		} catch (CommandExecutionException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(null, Messages.getString(Messages.SCENARIOS_EXECUTION_FAILED));
		} finally {
			resetButtonsAndState();
		}
	}

	private void setupRecordActionListeners() {

		this.recordButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Menu.this.recorderState != STOPPED) {
					toggleRecordState(null);
				} else {
					renameInputDialog();
				}
			}
		});

		this.screenshotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplayedMask();
				Menu.this.takePngScreenshot();
			}
		});
	}

	/**
	 * Plays the scenario stored in a file.
	 *
	 * @param subfolderName
	 *            name of the file to be played.
	 *
	 * @throws CommandExecutionException
	 *             when executing {@code CompareScreenshotCommand} and it fails the test.
	 */
	public void playFromFile(String subfolderName) throws CommandExecutionException {
		synchronized (LOCK) {
			playFromFileInternal(subfolderName, false);
		}
	}

	/**
	 * Updates the scenario stored in a file.
	 *
	 * @param subfolderName
	 *            name of the file to be played.
	 * @throws CommandExecutionException
	 *             when executing {@code CompareScreenshotCommand} and it fails the test.
	 */
	public void updateFromFile(String subfolderName) throws CommandExecutionException {
		synchronized (LOCK) {
			playFromFileInternal(subfolderName, true);
		}
	}

	private void playFromFileInternal(String subfolderName, boolean override) throws CommandExecutionException {
		if (this.recorderState != STOPPED) {
			return;
		}

		FileHelper.setSubfolder(subfolderName);
		FileHelper.createNewReportFile();

		RecorderPointer pointer = Device.getDevice().getWidget(RecorderPointer.class);
		PointerListener listener = pointer.getListener();
		if (listener != null) {
			File file = Paths.get(getFolder(), subfolderName, STEPS_FILE_NAME).toFile();
			try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {

				ArrayList<Command<?>> c = extractCommands(bufferedReader, override);

				int arraySize = c.size();

				if (this.progressBar != null) {
					this.progressBar.setMinimum(0);
					this.progressBar.setMaximum(arraySize - 1);
				}
				for (int i = this.playingCommandsIndex; i < c.size(); i++) {
					Command<?> command = c.get(i);
					if (this.playState == PAUSED) {
						break;
					}
					command.execute();
					if (this.playingCommandsIndex >= c.size() - 1) {
						this.playState = STOPPED;
						Menu.this.playButton.setIcon(Menu.this.playIcon);
						Menu.this.labelPlay.setText(PLAY);
					} else {
						this.playingCommandsIndex = this.playingCommandsIndex + 1;
					}
					if (this.progressBar != null) {
						this.progressBar.setValue(this.playingCommandsIndex);
					}
				}
			} catch (IOException e) {
				throw new CommandExecutionException(Messages.getString(Messages.READ_SCENARIO_FILE_ERROR), e);
			}
		}
	}

	private ArrayList<Command<?>> extractCommands(BufferedReader bufferedReader, boolean override)
			throws CommandExecutionException {
		ArrayList<Command<?>> commands = new ArrayList<>();
		String sentence;
		try {
			while ((sentence = bufferedReader.readLine()) != null) {
				Command<?> command = CommandBuilder.parse(sentence);
				if (override && command.getClass().equals(CompareScreenshotCommand.class)) {
					LOGGER.info("UpdateScreenshotCommand......."); //$NON-NLS-1$
					sentence = sentence.replace("compareScreenshot", "overrideScreenshot"); //$NON-NLS-1$//$NON-NLS-2$
					command = CommandBuilder.parse(sentence);
				}
				commands.add(command);
			}
		} catch (IOException e) {
			throw new CommandExecutionException(Messages.getString(Messages.READ_SCENARIO_FILE_ERROR), e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new CommandExecutionException(e);
		}
		return commands;
	}

	/**
	 * Saves the screen in a .png file under the subfolder used for the scenario being recorded.
	 */
	public void takePngScreenshot() {
		LOGGER.fine("Taking a PNG screenshot."); //$NON-NLS-1$

		if (this.recorderState == RECORDING) {

			String timestamp = LocalDateTime.now().toString();
			timestamp = timestamp.replace("-", EMPTY_TEXT).replace("T", EMPTY_TEXT).replace(":", EMPTY_TEXT); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			timestamp = timestamp.substring(0, timestamp.indexOf('.'));

			File screenshotPngFile = Paths
					.get(getFolder(), FileHelper.getSubfolder(),
							"screenshot" + timestamp + CompareScreenshotCommand.PNG_EXTENSION) //$NON-NLS-1$
					.toFile();

			BufferedImage rawImage = DeviceHelper.getDeviceRawImage();
			try {

				ImageIO.write(rawImage, CompareScreenshotCommand.PNG, screenshotPngFile);

				ArrayList<Mask> maskList = new ArrayList<>();
				if (this.enableMaskCheckbox.isSelected()) {
					maskList = getCurrentMaskList();
				}
				saveCommand(CompareScreenshotCommand.with(timestamp, maskList));

				// Disable mask after taking screenshot.
				this.enableMaskCheckbox.setSelected(false);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				JOptionPane.showMessageDialog(null, Messages.getString(Messages.SAVE_FILE_ERROR));
				toggleRecordState(null);
			} catch (NumberFormatException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private ArrayList<Mask> getCurrentMaskList() {

		ArrayList<Mask> maskList = new ArrayList<>();

		for (int i = 0; i < this.cbListMasks.getItemCount(); i++) {
			maskList.add(this.cbListMasks.getItemAt(i));
		}

		return maskList;
	}

	/**
	 * Saves the command on the current scenario being recorded. This requires the steps file to have been created
	 * already.
	 *
	 * @param command
	 *            the command to be written
	 */
	public void saveCommand(Command<?> command) {
		if (this.recorderState == RECORDING) {
			String commandToBeSaved = CommandBuilder.serialize(command);
			String serializedCommand = CommandBuilder.serialize(new WaitCommand()) + System.lineSeparator()
					+ commandToBeSaved;
			File file = Paths.get(this.folder, FileHelper.getSubfolder(), STEPS_FILE_NAME).toFile();
			try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file, true))) {
				dos.writeBytes(serializedCommand + System.lineSeparator());
				if (this.activityLogger != null) {
					this.activityLogger.push(serializedCommand.split(COMMA)[0]);
					this.activityLogger.push(commandToBeSaved.split(COMMA)[0]);
				}
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
				JOptionPane.showMessageDialog(null, Messages.getString(Messages.SAVE_FILE_ERROR));
				toggleRecordState(null);
			}
		}
	}

	/**
	 * The folder where all the files are stored.
	 *
	 * @return the folder where the data will be saved.
	 */
	public String getFolder() {
		return this.folder;
	}

	/**
	 * Sets the folder where all the files will be stored.
	 *
	 * @param folder
	 *            the folder where the data will be saved.
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
	 * Toggles recording state.
	 *
	 * @param scenarioName
	 *            the scenario name.
	 */
	public void toggleRecordState(String scenarioName) {
		try {

			if (Menu.this.recorderState != STOPPED) {
				Menu.this.activityLogger.push(Messages.getString(Messages.RECORDING_STOPPED));
				Menu.this.recordButton.setIcon(Menu.this.recordIcon);
				Menu.this.labelRecord.setText(START_RECORD);

				createOrUpdateScenarioList();
				Menu.this.screenshotButton.setEnabled(false);
				Menu.this.playButton.setEnabled(true);
				Menu.this.overrideButton.setEnabled(true);
				Menu.this.playButton.setIcon(Menu.this.playIcon);

				Menu.this.labelPlay.setText(PLAY);
				Menu.this.recorderState = STOPPED;

			} else {

				WaitCommand.updateLastEventTime();

				FileHelper.createNewCommandFile(scenarioName);

				Menu.this.recordButton.setIcon(Menu.this.stopIcon);
				Menu.this.labelRecord.setText(STOP_RECORD);
				Menu.this.activityLogger.push(Messages.getString(Messages.RECORDING_STARTED));
				Menu.this.screenshotButton.setEnabled(true);
				Menu.this.playButton.setEnabled(true);
				Menu.this.overrideButton.setEnabled(false);
				Menu.this.playButton.setIcon(Menu.this.pauseIcon);
				Menu.this.labelPlay.setText(PAUSE);
				Menu.this.recorderState = RECORDING;
			}

			handleMaskDisplay(true);

		} catch (IOException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage(), e);
			JOptionPane.showMessageDialog(null, Messages.getString(Messages.SAVE_FILE_ERROR));
		}
	}

	private void createOrUpdateScenarioList() {
		String[] fileList = getFolderFileList();

		if (this.scenarioList == null) {
			// Create scenario comboBox if not created.
			this.itemCount = fileList.length;
			this.scenarioList = new JComboBox<>(fileList);
			this.scenarioList.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.SCENARIO_LIST_Y + MenuSizes.Y_OFFSET,
					MenuSizes.SCENARIO_LIST_WIDTH, MenuSizes.SCENARIO_LIST_HEIGHT);
		} else if (Menu.this.itemCount != fileList.length) {
			// Update scenario list if changed.
			Menu.this.itemCount = fileList.length;
			Menu.this.scenarioList.setModel(new DefaultComboBoxModel<>(fileList));
		}

		if (fileList.length == 0 && this.playButton != null) {
			this.playButton.setEnabled(false);
		}
	}

	private JPanel createMaskEditContainer() {

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		panel.setBackground(AppColors.getLightGrey());
		panel.setPreferredSize(
				new Dimension(this.containerWidth, MenuSizes.MASK_EDIT_ZONE_HEIGHT + MenuSizes.MASK_PANEL_SPACING));

		JPanel maskContainer = new JPanel(new GridBagLayout());
		maskContainer.setBackground(AppColors.getLightGrey());
		maskContainer.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				Messages.getString(Messages.MASK_EDIT)));

		GridBagConstraints constraints = getVerticalGridBagConstraints();

		JPanel maskPanel = new JPanel();
		maskPanel.setLayout(new BoxLayout(maskPanel, BoxLayout.Y_AXIS));

		maskPanel.setBackground(AppColors.getLightGrey());

		maskPanel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		maskPanel.setBounds(MenuSizes.MARGIN_LEFT, 0, this.containerWidth, MenuSizes.MASK_EDIT_ZONE_HEIGHT);

		this.enableMaskCheckbox = new JCheckBox(Messages.getString(Messages.ENABLE_MASK));
		this.enableMaskCheckbox.setBackground(AppColors.getLightGrey());

		this.enableMaskCheckbox.setSelected(false);
		this.enableMaskCheckbox.setVerticalAlignment(SwingConstants.TOP);

		this.enableMaskCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox cb = (JCheckBox) event.getSource();
				setMaskEnabled(cb.isSelected());
				setListMaskEnabled(cb.isSelected());

				if (cb.isSelected()) {

					if (Menu.this.showAllMaskCheckbox.isSelected()) {
						setCurrentImageWithMask(getCurrentDisplayedImageWithAllCreatedMask());
					} else {
						setCurrentImageWithMask(getCurrentDisplayedImageWithMaskEditValues());
					}

					if (cb.isSelected() && !Menu.this.firstTimeEnableMaskChecked) {
						Menu.this.firstTimeEnableMaskChecked = true;
						JFrame frameAlert = new JFrame();
						JOptionPane.showMessageDialog(frameAlert, MASK_ENABLED_MSG, MASK_ENABLED,
								JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		});

		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
		DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
		decimalFormat.setGroupingUsed(false);

		// creating the mask properties text fields.
		this.maskXTextField = new JFormattedTextField(decimalFormat);

		this.maskXTextField.setText(ZERO_TEXT);
		this.maskXTextField.setEditable(false);
		this.maskXTextField.setColumns(MenuSizes.MASK_TEXT_FIELD_SIZE);

		this.maskYTextField = new JFormattedTextField(decimalFormat);
		this.maskYTextField.setText(ZERO_TEXT);
		this.maskYTextField.setEditable(false);
		this.maskYTextField.setColumns(MenuSizes.MASK_TEXT_FIELD_SIZE);

		this.maskWidthTextField = new JFormattedTextField(decimalFormat);
		this.maskWidthTextField.setText(ZERO_TEXT);
		this.maskWidthTextField.setEditable(false);
		this.maskWidthTextField.setColumns(MenuSizes.MASK_TEXT_FIELD_SIZE);

		this.maskHeightTextField = new JFormattedTextField(decimalFormat);
		this.maskHeightTextField.setText(ZERO_TEXT);
		this.maskHeightTextField.setEditable(false);
		this.maskHeightTextField.setColumns(MenuSizes.MASK_TEXT_FIELD_SIZE);

		maskPanel.add(createMaskEditRow(MenuSizes.MASK_EDIT_FIRST_ROW_Y, Messages.getString(Messages.MASK_X),
				this.maskXTextField, Messages.getString(Messages.MASK_Y), this.maskYTextField));

		maskPanel.add(createMaskEditRow(MenuSizes.MASK_EDIT_SECOND_ROW_Y, Messages.getString(Messages.MASK_WIDTH),
				this.maskWidthTextField, Messages.getString(Messages.MASK_HEIGHT), this.maskHeightTextField));

		JPanel maskCheckBoxContainer = new JPanel(new GridLayout(2, 1));
		maskCheckBoxContainer.setBackground(AppColors.getLightGrey());
		maskCheckBoxContainer.setBorder(new EmptyBorder(0, 0, 0, 0));
		maskCheckBoxContainer.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		maskCheckBoxContainer.setPreferredSize(
				new Dimension(MenuSizes.WIDTH_MASK_CHECKBOX_CONTAINER, (MenuSizes.HEIGHT_MASK_CHECKBOX_CONTAINER)));

		JLabel nameColors = new JLabel(Messages.getString(Messages.COLORS));
		nameColors.setBackground(AppColors.getLightGrey());

		String[] clorsMask = { AUTO, RED, GREEN, BLUE, PINK, WHITE, BLACK, YELLOW, ORANGE };

		this.colorsMaskList = new JComboBox<>(clorsMask);

		this.colorsMaskList.setBounds(MenuSizes.COLORS_LIST_Y, 0, MenuSizes.COLORS_WIDTH, MenuSizes.COLORS_HEIGHT);
		this.colorsMaskList.setPreferredSize(new Dimension(MenuSizes.COLORS_WIDTH, MenuSizes.COLORS_HEIGHT));

		this.colorsMaskList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplayedMask();
			}
		});

		// container panel mask colors
		JPanel colorsMaskContainerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		colorsMaskContainerPanel.setBounds(0, 0, MenuSizes.COLORS_MASK_CONTAINER_WIDTH,
				MenuSizes.COLORS_MASK_CONTAINER_HEIGHT);
		colorsMaskContainerPanel.setPreferredSize(
				new Dimension(MenuSizes.COLORS_MASK_CONTAINER_WIDTH, MenuSizes.COLORS_MASK_CONTAINER_HEIGHT));

		colorsMaskContainerPanel.setBackground(AppColors.getLightGrey());

		JPanel colorsMaskPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		colorsMaskPanel.setBounds(0, 0, MenuSizes.COLORS_MASK_PANEL_WIDTH, MenuSizes.COLORS_MASK_PANEL_HEIGHT);
		colorsMaskPanel
				.setPreferredSize(new Dimension(MenuSizes.COLORS_MASK_PANEL_WIDTH, MenuSizes.COLORS_MASK_PANEL_HEIGHT));
		colorsMaskPanel.setLayout(new BoxLayout(colorsMaskPanel, BoxLayout.X_AXIS));
		colorsMaskPanel.setBackground(AppColors.getLightGrey());
		colorsMaskPanel.add(nameColors);
		colorsMaskPanel.add(this.colorsMaskList);

		colorsMaskContainerPanel.add(colorsMaskPanel);

		this.enableMaskCheckbox.setBounds(0, 0, MenuSizes.WIDTH_MASK_CHECKBOX, MenuSizes.HEIGHT_MASK_CHECKBOX);
		maskCheckBoxContainer.add(this.enableMaskCheckbox);

		maskCheckBoxContainer.add(colorsMaskContainerPanel);

		maskContainer.add(maskCheckBoxContainer, constraints);
		maskContainer.add(maskPanel, constraints);

		panel.add(maskContainer);

		setupMaskListener();
		return panel;
	}

	private GridBagConstraints getVerticalGridBagConstraints() {

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 5 /* margin */, 0);
		constraints.weightx = 1.0;
		constraints.gridy = 1;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(0, 0, 0, 0);

		return constraints;
	}

	/**
	 * Gets the current selected color from list. If Auto is selected it returns -1 value;
	 *
	 * @return the RGB color.
	 */
	public int getCurrentSelectedColorMask() {

		String color = (String) this.colorsMaskList.getSelectedItem();
		switch (color) {
		case RED:
			return Color.RED.getRGB();
		case GREEN:
			return Color.GREEN.getRGB();
		case BLUE:
			return Color.BLUE.getRGB();
		case PINK:
			return Color.PINK.getRGB();
		case WHITE:
			return WHITE_COLOR;
		case BLACK:
			return Color.BLACK.getRGB();
		case YELLOW:
			return Color.YELLOW.getRGB();
		case ORANGE:
			return Color.ORANGE.getRGB();
		case AUTO:
		default:
			return AUTO_COLOR;
		}
	}

	private void setupMaskListener() {

		this.maskXTextField.addCaretListener(this);

		this.maskYTextField.addCaretListener(this);

		this.maskWidthTextField.addCaretListener(this);

		this.maskHeightTextField.addCaretListener(this);

	}

	private JPanel createListMasksPanel() {

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.PATH_ZONE_Y + MenuSizes.SPACING, this.containerWidth,
				MenuSizes.PATH_ZONE_HEIGHT - MenuSizes.SPACING_MASKS_PANEL);

		panel.setBackground(AppColors.getLightGrey());

		panel.setPreferredSize(
				new Dimension(this.containerWidth - MenuSizes.SPACING, PATH_TEXT_FIELD_HEIGHT + MenuSizes.SPACING));

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				Messages.getString(Messages.MASK_LIST)));
		panel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		Mask[] nameMasks = {};

		Menu.this.cbListMasks = new JComboBox<>(nameMasks);

		this.cbListMasks.setBounds(MenuSizes.CB_LIST_MASKS_X, MenuSizes.CB_LIST_MASKS_Y, MenuSizes.CB_LIST_MASKS_WIDTH,
				MenuSizes.CB_LIST_MASKS_HEIGHT);

		this.cbListMasks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				setMaskInfoInFields(Menu.this.cbListMasks.getSelectedIndex());
			}
		});

		JPanel panelListMasks = new JPanel();

		panelListMasks.setLayout(new BoxLayout(panelListMasks, BoxLayout.X_AXIS));
		panelListMasks.setBackground(AppColors.getLightGrey());
		panelListMasks.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.btnDelete = new JButton(Messages.getString(Messages.DELETE));
		this.btnDelete.setSize(MenuSizes.BUTTON_WIDTH, MenuSizes.BUTTON_HEIGHT);

		this.btnAdd = new JButton(Messages.getString(Messages.ADD_NEW));
		this.btnAdd.setSize(MenuSizes.BUTTON_WIDTH, MenuSizes.BUTTON_HEIGHT);

		this.btnRename = new JButton(Messages.getString(Messages.RENAME_BUTTON));
		this.btnRename.setSize(MenuSizes.BUTTON_WIDTH, MenuSizes.BUTTON_HEIGHT);

		this.showAllMaskCheckbox = new JCheckBox(Messages.getString(Messages.SHOW_ALL_MASKS));
		this.showAllMaskCheckbox.setBackground(AppColors.getLightGrey());

		this.showAllMaskCheckbox.setSelected(false);

		this.showAllMaskCheckbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (Menu.this.cbListMasks != null && Menu.this.cbListMasks.getItemCount() > 0) {
					JCheckBox cb = (JCheckBox) event.getSource();
					showMasks(cb.isSelected());
				}
			}
		});

		this.btnDelete.setEnabled(false);
		this.btnAdd.setEnabled(false);
		this.btnRename.setEnabled(false);
		this.showAllMaskCheckbox.setEnabled(false);
		this.cbListMasks.setEnabled(false);

		this.btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {

				if (Menu.this.enableMaskCheckbox.isSelected()) {

					JFrame frame = new JFrame();

					String nameMask = JOptionPane.showInputDialog(frame, ENTER_MASK_NAME);

					if (nameMask != null) {
						if (!nameMask.isEmpty()) {
							int maskX = Integer.parseInt(Menu.this.maskXTextField.getText());
							int maskY = Integer.parseInt(Menu.this.maskYTextField.getText());
							int maskWidth = Integer.parseInt(Menu.this.maskWidthTextField.getText());
							int maskHeight = Integer.parseInt(Menu.this.maskHeightTextField.getText());

							int maskIndex = Menu.this.cbListMasks.getItemCount();
							Mask mask = new Mask(maskIndex, nameMask, maskX, maskY, maskWidth, maskHeight);
							Menu.this.cbListMasks.addItem(mask);
							Menu.this.cbListMasks.setSelectedIndex(maskIndex);
						} else {
							JFrame frameAlert = new JFrame();
							JOptionPane.showMessageDialog(frameAlert, ENTER_VALID_NAME, ALERT,
									JOptionPane.WARNING_MESSAGE);
						}
					}

				}
			}

		});

		this.btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {

				Menu.this.cbListMasks.removeItem(Menu.this.cbListMasks.getSelectedItem());
				updateDisplayedMask();
				if (Menu.this.cbListMasks.getItemCount() == 0) {
					resetDisplayedMask();
				}
			}

		});

		this.btnRename.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				renameMask();
			}

		});

		panelListMasks.add(this.cbListMasks);
		panelListMasks.add(this.showAllMaskCheckbox);
		panelListMasks.add(this.btnRename);
		panelListMasks.add(this.btnDelete);
		panelListMasks.add(this.btnAdd);
		panel.add(panelListMasks);

		return panel;
	}

	private void setMaskInfoInFields(int selectedIndex) {

		Mask selectedMask = this.cbListMasks.getItemAt(selectedIndex);
		if (selectedMask != null) {
			Menu.this.maskXTextField.setText(String.valueOf(selectedMask.getX()));
			Menu.this.maskYTextField.setText(String.valueOf(selectedMask.getY()));
			Menu.this.maskWidthTextField.setText(String.valueOf(selectedMask.getWidth()));
			Menu.this.maskHeightTextField.setText(String.valueOf(selectedMask.getHeight()));
		}

	}

	private void renameMask() {

		JFrame frame = new JFrame();
		String newNameMask = JOptionPane.showInputDialog(frame, ENTER_MASK_NEW_NAME);
		if (newNameMask != null) {
			if (!newNameMask.isEmpty()) {
				this.cbListMasks.getItemAt(this.cbListMasks.getSelectedIndex()).setTitle(newNameMask);
			} else {
				JFrame frameAlert = new JFrame();
				JOptionPane.showMessageDialog(frameAlert, ENTER_VALID_NAME, ALERT, JOptionPane.WARNING_MESSAGE);
			}
		}

	}

	private void showMasks(boolean didShowAllMasks) {

		if (didShowAllMasks) {
			File imageWithMasks = getCurrentDisplayedImageWithAllCreatedMask();
			if (imageWithMasks != null) {
				setCurrentImageWithMask(imageWithMasks);
			}
		} else {
			Mask mask = this.cbListMasks.getItemAt(this.cbListMasks.getSelectedIndex());
			setCurrentImageWithMask(
					getCurrentDisplayedImageWithMask(mask.getX(), mask.getY(), mask.getWidth(), mask.getHeight()));
		}
	}

	private JPanel createMaskEditRow(int yPos, String leftLabel, JFormattedTextField leftEditText, String rightLabel,
			JFormattedTextField rightEditText) {
		JPanel row = new JPanel(new GridLayout(1, 2));

		row.setBorder(new EmptyBorder(0, MenuSizes.MASK_EDIT_ROW_MARGIN_LEFT, 0, 0));
		row.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		row.setBounds(MenuSizes.MASK_TEXT_FIELDS_X, yPos, MenuSizes.MASK_TEXT_FIELDS_WIDTH,
				MenuSizes.MASK_TEXT_FIELDS_HEIGHT);
		row.setBackground(AppColors.getLightGrey());

		JPanel leftCol = new JPanel(new GridLayout(1, MenuSizes.NUMBER_OF_COLUMNS));
		leftCol.setBackground(AppColors.getLightGrey());

		JPanel rightCol = new JPanel(new GridLayout(1, MenuSizes.NUMBER_OF_COLUMNS));
		rightCol.setBackground(AppColors.getLightGrey());

		leftCol.add(new JLabel(leftLabel));
		JPanel xPanelEdit = new JPanel(new FlowLayout(FlowLayout.LEFT));
		xPanelEdit.setBackground(AppColors.getLightGrey());
		xPanelEdit.add(leftEditText);
		leftCol.add(xPanelEdit);

		rightCol.add(new JLabel(rightLabel));

		JPanel yPanelEdit = new JPanel(new FlowLayout(FlowLayout.LEFT));
		yPanelEdit.setBackground(AppColors.getLightGrey());
		yPanelEdit.add(rightEditText);
		rightCol.add(yPanelEdit);

		row.add(leftCol);
		row.add(rightCol);

		return row;
	}

	private void setMaskEnabled(boolean enabled) {
		if (this.maskXTextField != null && this.maskYTextField != null && this.maskWidthTextField != null
				&& this.maskHeightTextField != null) {

			this.maskXTextField.setEditable(enabled);
			this.maskYTextField.setEditable(enabled);
			this.maskWidthTextField.setEditable(enabled);
			this.maskHeightTextField.setEditable(enabled);

			handleMaskDisplay(enabled);
		}
	}

	private void setListMaskEnabled(boolean enabled) {

		this.showAllMaskCheckbox.setEnabled(enabled);

		resetDisplayedMask();
		this.cbListMasks.setEnabled(enabled);
		this.btnAdd.setEnabled(enabled);
		this.btnDelete.setEnabled(enabled);
		this.btnRename.setEnabled(enabled);

	}

	private void handleMaskDisplay(boolean didShowMaskZone) {

		if (this.state == ActionState.PLAY || this.state == ActionState.UPDATE) {
			this.maskShowMainPanel.setVisible(true);

			this.mainScreenshotPanel.setVisible(true);
		} else if (didShowMaskZone) {
			this.mainScreenshotPanel.setVisible(true);
			this.maskShowMainPanel.setVisible(true);
			updateDisplayedMask();
		}
	}

	private void updateDisplayedMask() {

		if (this.enableMaskCheckbox.isSelected()) {

			int maskX = this.maskXTextField.getText().isEmpty() ? 0 : Integer.parseInt(this.maskXTextField.getText());
			int maskY = this.maskYTextField.getText().isEmpty() ? 0 : Integer.parseInt(this.maskYTextField.getText());
			int maskWidth = this.maskWidthTextField.getText().isEmpty() ? 0
					: Integer.parseInt(this.maskWidthTextField.getText());
			int maskHeight = this.maskHeightTextField.getText().isEmpty() ? 0
					: Integer.parseInt(this.maskHeightTextField.getText());

			if (this.showAllMaskCheckbox.isSelected()) {
				File theCurrentMaskList = getCurrentDisplayedImageWithAllMasks(maskX, maskY, maskWidth, maskHeight);
				setCurrentImageWithMask(theCurrentMaskList);
			} else {
				setCurrentImageWithMask(getCurrentDisplayedImageWithMask(maskX, maskY, maskWidth, maskHeight));
			}
		} else {
			resetDisplayedMask();
		}

	}

	private void resetDisplayedMask() {

		int maskX = 0;
		int maskY = 0;
		int maskWidth = 0;
		int maskHeight = 0;
		setCurrentImageWithMask(getCurrentDisplayedImageWithMask(maskX, maskY, maskWidth, maskHeight));

	}

	private JPanel createScreenshotCompareContainer() {
		this.mainScreenshotPanel = new JPanel();
		this.mainScreenshotPanel.setLayout(new BoxLayout(this.mainScreenshotPanel, BoxLayout.Y_AXIS));
		this.mainScreenshotPanel.setBackground(AppColors.getLightGrey());
		this.mainScreenshotPanel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.mainScreenshotPanel.setBounds(MenuSizes.MARGIN_LEFT,
				MenuSizes.MAIN_SCREENSHOT_PANEL_Y + MenuSizes.Y_OFFSET, this.containerWidth, this.maskContainerHeight);
		this.mainScreenshotPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				Messages.getString(Messages.COMPARE_MASK)));

		this.mainScreenshotPanel.add(createCompareScreenshotTitle());
		this.mainScreenshotPanel.add(createCompareScreenshotPanel(null, null));
		this.mainScreenshotPanel.add(createComparePercentPanel());
		return this.mainScreenshotPanel;
	}

	private JPanel createCompareScreenshotTitle() {

		JPanel panelTitle = new JPanel(new GridLayout(1, 2));

		panelTitle.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.COMPARE_TITLE_Y, this.containerWidth,
				MenuSizes.COMPARE_TITLE_HEIGHT);

		panelTitle.setBackground(Color.lightGray);

		// Create label current screenshot
		JLabel labelScreenshot = new JLabel(Messages.getString(Messages.CURRENT_SCREENSHOT));
		labelScreenshot.setBackground(Color.WHITE);

		if (isSmallContainer()) {
			labelScreenshot.setBorder(new EmptyBorder(0, MenuSizes.SCREENSHOT_MARGIN_LEFT_SMALL, 0, 0));
		} else {
			labelScreenshot.setBorder(new EmptyBorder(0, MenuSizes.SCREENSHOT_MARGIN_LEFT_BIG, 0, 0));
		}

		panelTitle.add(labelScreenshot);

		// Create label current screenshot
		JLabel labeltoCompare = new JLabel(Messages.getString(Messages.IMAGE_TO_COMPARE));

		if (isSmallContainer()) {
			labeltoCompare.setBorder(new EmptyBorder(0, MenuSizes.SCREENSHOT_MARGIN_LEFT_SMALL, 0, 0));
		} else {
			labeltoCompare.setBorder(new EmptyBorder(0, MenuSizes.SCREENSHOT_MARGIN_LEFT_BIG, 0, 0));
		}

		labeltoCompare.setBackground(Color.WHITE);
		panelTitle.add(labeltoCompare);
		return panelTitle;
	}

	private JPanel createCompareScreenshotPanel(File fileA, File fileB) {

		JPanel panel = new JPanel(new GridLayout(1, 2));
		int panelHeight = this.displayHeight;
		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.COMPARE_SCREENSHOT_PANEL_Y, this.containerWidth, panelHeight);
		panel.setBackground(Color.lightGray);
		setCurrentImage(fileA);
		panel.add(this.currentImage);
		setComparedImage(fileB);
		panel.add(this.comparedImage);
		return panel;
	}

	private JPanel createComparePercentPanel() {
		JPanel panelPercent = new JPanel();
		panelPercent.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.COMPARE_PERCENT_Y, this.containerWidth,
				MenuSizes.COMPARE_PERCENT_HEIGHT);
		panelPercent.setBackground(Color.lightGray);
		setComparePercent(0);
		panelPercent.add(this.comparePercentLabel);
		return panelPercent;
	}

	/**
	 * Sets the current image file.
	 *
	 * @param file
	 *            the image file.
	 *
	 */
	public void setCurrentImage(File file) {
		ImageIcon icon = getImageIconFromFile(file);
		if (this.currentImage == null) {

			if (icon != null) {
				this.currentImage = new JLabel(getImageIconFromFile(file));
			} else {
				this.currentImage = new JLabel(new ImageIcon(Menu.class.getResource("/icons/no_screenshot.png"))); //$NON-NLS-1$
			}

		} else {
			if (icon != null) {
				this.currentImage.setIcon(getImageIconFromFile(file));
			} else {
				this.currentImage.setIcon(new ImageIcon(Menu.class.getResource("/icons/no_screenshot.png"))); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Sets the compared image file.
	 *
	 * @param file
	 *            the image file.
	 *
	 */
	public void setComparedImage(File file) {
		ImageIcon icon = getImageIconFromFile(file);
		if (this.comparedImage == null) {
			if (icon != null) {
				this.comparedImage = new JLabel(getImageIconFromFile(file));
			} else {
				this.comparedImage = new JLabel(new ImageIcon(Menu.class.getResource("/icons/no_screenshot.png"))); //$NON-NLS-1$
			}
		} else {
			if (icon != null) {
				this.comparedImage.setIcon(getImageIconFromFile(file));
			} else {
				this.comparedImage.setIcon(new ImageIcon(Menu.class.getResource("/icons/no_screenshot.png"))); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Display the compare percent.
	 *
	 * @param percent
	 *            the percent to display.
	 */
	public void setComparePercent(float percent) {
		float rate = percent / 100;
		double rateFormated = Math.round(rate * 100) / 100.0;

		String displayedText = "Differences rate between the 2 screenshots: " + rateFormated; //$NON-NLS-1$
		if (this.comparePercentLabel == null) {
			this.comparePercentLabel = new JLabel(displayedText);
			this.comparePercentLabel.setBackground(Color.WHITE);
		} else {
			this.comparePercentLabel.setText(displayedText);
		}
		if (percent < HUNDRED_PERCENT) {
			this.comparePercentLabel.setForeground(Color.RED);
		} else {
			this.comparePercentLabel.setForeground(AppColors.getGreenColor());
		}
	}

	private ImageIcon getImageIconFromFile(File file) {
		if (file != null) {
			try {
				BufferedImage img;
				img = ImageIO.read(file);
				if (img != null) {
					ImageIcon icon = new ImageIcon(img);
					java.awt.Image image = icon.getImage();
					// resize image
					java.awt.Image resizeimg = image.getScaledInstance(this.displayWidth, this.displayHeight,
							java.awt.Image.SCALE_SMOOTH);
					return new ImageIcon(resizeimg);
				} else {
					return null;
				}
			} catch (IOException e) {
				LOGGER.log(Level.OFF, e.getMessage(), e);
			}
		}
		return null;
	}

	private void setupMockDimensions() {
		Display display = Device.getDevice().getWidget(Display.class, null);

		this.displayWidth = display.getWidth();
		this.displayHeight = display.getHeight();
		float ratio = (float) display.getHeight() / (float) display.getWidth();
		if (display.getWidth() > MenuSizes.DISPLAY_MAX_WIDTH) {
			// MAX WIDTH is 320px
			this.displayWidth = MenuSizes.DISPLAY_MAX_WIDTH;
			this.displayHeight = (int) (this.displayWidth * ratio);
			this.containerWidth = MenuSizes.MAX_WIDTH;
		} else if (display.getWidth() < MenuSizes.DISPLAY_MIN_WIDTH) {
			// MIN WIDTH is 130px
			this.displayWidth = MenuSizes.DISPLAY_MIN_WIDTH;
			this.displayHeight = (int) (this.displayWidth * ratio);
			this.containerWidth = MenuSizes.MIN_WIDTH;
		} else {
			this.containerWidth = display.getWidth() * 2 + MenuSizes.MARGIN_MOCK_SCREENSHOT;
		}

		this.maskContainerHeight = this.displayHeight + MenuSizes.COMPARE_TITLE_HEIGHT
				+ MenuSizes.COMPARE_PERCENT_HEIGHT + MenuSizes.COMPARE_ZONE_VERTICAL_MARGIN;
		this.mockWidth = this.containerWidth + MenuSizes.MARGIN_MOCK_SCREENSHOT;
	}

	private boolean isSmallContainer() {
		return this.containerWidth == MenuSizes.MIN_WIDTH;
	}

	private JButton createButton(Icon icon) {
		JButton button = new JButton(icon);
		button.setBackground(Color.WHITE);
		button.setBorder(this.emptyBorder);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setSize(MenuSizes.BUTTONS_SIZE, MenuSizes.BUTTONS_SIZE);
		return button;
	}

	private JPanel createPanelSenario() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.PATH_ZONE_Y + MenuSizes.SPACING, this.containerWidth,
				MenuSizes.PATH_ZONE_HEIGHT);

		panel.setBackground(AppColors.getLightGrey());
		panel.setPreferredSize(
				new Dimension(this.containerWidth - MenuSizes.SPACING, PATH_TEXT_FIELD_HEIGHT + MenuSizes.SPACING));

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				Messages.getString(Messages.SCENARIO_LIST)));
		panel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		JPanel scenarioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		scenarioPanel.setLayout(new BoxLayout(scenarioPanel, BoxLayout.Y_AXIS));
		scenarioPanel.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		scenarioPanel.setPreferredSize(
				new Dimension(this.containerWidth - MenuSizes.SPACING, MenuSizes.SCENARIO_LIST_HEIGHT));
		scenarioPanel.setBackground(AppColors.getLightGrey());
		scenarioPanel.add(createScenarioListPanel());
		panel.add(scenarioPanel);
		return panel;
	}

	private JTabbedPane createTabbedPane() {

		// JPanel 1
		JPanel panelSettings = new JPanel();
		panelSettings.setLayout(new BorderLayout());
		panelSettings.setLayout(new BoxLayout(panelSettings, BoxLayout.Y_AXIS));

		panelSettings.add(createPathEditContainer());
		panelSettings.add(createPanelSenario());
		panelSettings.add(createEmptyContainer());

		JPanel maskEditAndLists = new JPanel();
		maskEditAndLists.setLayout(new BoxLayout(maskEditAndLists, BoxLayout.Y_AXIS));
		maskEditAndLists.add(createMaskEditContainer());
		maskEditAndLists.add(createListMasksPanel());

		JPanel panelMask = createVerticalContainer(maskEditAndLists, createScreenshotMaskShowContainer());

		// JPanel 3
		JPanel panelScreenshot = new JPanel();
		panelScreenshot.add(createScreenshotCompareContainer());
		JTabbedPane onglets = new JTabbedPane();

		onglets.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.JTABBEDPANE_Y, this.containerWidth,
				MenuSizes.JTABBEDPANE_HEIGHT);

		onglets.add(Messages.getString(Messages.SETTINGS), panelSettings);
		onglets.add(Messages.getString(Messages.MASK), panelMask);
		onglets.add(Messages.getString(Messages.SCREENSHOT_COMPARAISON), panelScreenshot);

		return onglets;
	}

	private JPanel createVerticalContainer(JPanel panelA, JPanel panelB) {
		JPanel verticalPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.PAGE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 0, 5 /* margin */, 0);
		constraints.weightx = 1.0;
		verticalPanel.add(panelA, constraints);
		constraints.gridy = 1;
		constraints.weighty = 1.0;
		constraints.insets = new Insets(0, 0, 0, 0);
		verticalPanel.add(panelB, constraints);
		return verticalPanel;
	}

	private JPanel createEmptyContainer() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.PATH_ZONE_Y + MenuSizes.SPACING, this.containerWidth,
				MenuSizes.PANEL_HEIGHT);
		panel.setBackground(AppColors.getLightGrey());
		panel.setPreferredSize(new Dimension(this.containerWidth, MenuSizes.PANEL_HEIGHT));

		return panel;
	}

	private JPanel createPathEditContainer() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				Messages.getString(Messages.SCENARIO_PATH)));
		panel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.PATH_ZONE_Y + MenuSizes.SPACING, this.containerWidth,
				MenuSizes.PATH_ZONE_HEIGHT);

		panel.setBackground(AppColors.getLightGrey());
		panel.setPreferredSize(new Dimension(this.containerWidth, PATH_TEXT_FIELD_HEIGHT + MenuSizes.SPACING));

		JPanel pathPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pathPanel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		pathPanel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.PATH_ZONE_CONTENT_Y, MenuSizes.PATH_ZONE_CONTENT_WIDTH,
				MenuSizes.PATH_ZONE_CONTENT_HEIGHT);
		pathPanel.setBackground(AppColors.getLightGrey());

		// creating the path text field.
		this.pathTextField = new JTextField();
		this.pathTextField.setText(getFolder());
		this.pathTextField.setEditable(false);
		int pathWidth = ((this.containerWidth / 2) < MenuSizes.MIN_PATH_TEXT_FIELD_WIDTH)
				? MenuSizes.MIN_PATH_TEXT_FIELD_WIDTH
				: (this.containerWidth / 2);
		this.pathTextField.setPreferredSize(new Dimension(pathWidth, PATH_TEXT_FIELD_HEIGHT));

		// creating the browse button.
		this.browseButton = new JButton(Messages.getString(Messages.BROWSE));
		this.browseButton.setHorizontalAlignment(SwingConstants.LEFT);

		// adding the buttons to frame

		pathPanel.add(this.pathTextField);
		pathPanel.add(this.browseButton);
		panel.add(pathPanel);

		this.browseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				openScenarioPathDialog();
			}
		});

		return panel;
	}

	private void openScenarioPathDialog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int fileChooserState = fileChooser.showOpenDialog(Menu.this.browseButton);
		if (fileChooserState == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			String selectedFilepath = selectedFile.getPath();
			setFolder(selectedFilepath);
			Menu.this.pathTextField.setText(getFolder());
			createOrUpdateScenarioList();
		}
	}

	private void setupPath() {
		String defaultPath = System.getProperty(PATH_KEY);
		if (defaultPath == null) {
			defaultPath = Paths.get(System.getProperty("user.home"), ".microej", "frontPanelRecorder") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					.toAbsolutePath().toString();
		}
		setFolder(defaultPath);
	}

	private JPanel createScreenshotMaskShowContainer() {
		this.maskShowMainPanel = new JPanel();
		this.maskShowMainPanel.setLayout(new BoxLayout(this.maskShowMainPanel, BoxLayout.Y_AXIS));
		this.maskShowMainPanel.setBackground(AppColors.getLightGrey());
		this.maskShowMainPanel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		this.maskShowMainPanel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.MAIN_SCREENSHOT_PANEL_Y + MenuSizes.Y_OFFSET,
				this.containerWidth, this.maskContainerHeight);

		this.maskShowMainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				Messages.getString(Messages.SHOW_MASK)));
		this.maskShowMainPanel.add(createShowMaskPanel());
		this.maskShowMainPanel.setVisible(true);
		return this.maskShowMainPanel;
	}

	private JPanel createShowMaskPanel() {

		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		int panelHeight = this.displayHeight;
		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.COMPARE_SCREENSHOT_PANEL_Y, this.containerWidth, panelHeight);

		panel.setBackground(AppColors.getLightGrey());
		setCurrentImageWithMask(getCurrentDisplayedImageWithMask(0, 0, 0, 0));
		JButton reloadButton = new JButton(Messages.getString(Messages.RELOAD_IMAGE));
		reloadButton.setHorizontalAlignment(SwingConstants.CENTER);
		reloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.currentImageMask.setHorizontalAlignment(SwingConstants.CENTER);
		this.currentImageMask.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(reloadButton, BorderLayout.CENTER);
		panel.add(this.currentImageMask, BorderLayout.CENTER);
		reloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateDisplayedMask();
			}
		});

		return panel;
	}

	private JPanel createScenarioListPanel() {

		JPanel panelSenarioList = new JPanel(new BorderLayout());
		panelSenarioList.setLayout(new BoxLayout(panelSenarioList, BoxLayout.X_AXIS));
		panelSenarioList.setAlignmentX(Component.BOTTOM_ALIGNMENT);

		panelSenarioList.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		int panelHeight = this.displayHeight;
		panelSenarioList.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.COMPARE_SCREENSHOT_PANEL_Y, this.containerWidth,
				panelHeight);

		panelSenarioList.setAlignmentY(Component.CENTER_ALIGNMENT);

		panelSenarioList.setBackground(AppColors.getLightGrey());

		String[] fileList = getFolderFileList();

		this.itemCount = fileList.length;
		this.scenarioList = new JComboBox<>(fileList);
		this.scenarioList.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.SCENARIO_LIST_Y + MenuSizes.Y_OFFSET,
				MenuSizes.SCENARIO_LIST_WIDTH, MenuSizes.SCENARIO_LIST_HEIGHT);
		this.scenarioList
				.setPreferredSize(new Dimension(MenuSizes.SCENARIOS_LIST_WIDTH, MenuSizes.SCENARIO_LIST_HEIGHT));

		// creating the rename button.
		JButton renameButton;
		renameButton = new JButton(Messages.getString(Messages.RENAME));
		renameButton.setHorizontalAlignment(SwingConstants.LEFT);

		int renameButtonWidth = MenuSizes.RENAME_BUTTON_X;

		renameButton.setBounds(renameButtonWidth, MenuSizes.RENAME_BUTTON_Y + MenuSizes.Y_OFFSET,
				MenuSizes.RENAME_BUTTON_WIDTH, MenuSizes.RENAME_BUTTON_HEIGHT);
		renameButton.setPreferredSize(new Dimension(MenuSizes.RENAME_BUTTON_WIDTH, MenuSizes.RENAME_BUTTON_HEIGHT));

		panelSenarioList.add(this.scenarioList);
		panelSenarioList.add(renameButton);

		renameButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				renameScenario();
			}
		});

		return panelSenarioList;
	}

	private JPanel createProgressBarPanel() {

		JPanel panel = new JPanel(new BorderLayout());

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		panel.setBounds(MenuSizes.MARGIN_LEFT, MenuSizes.PROGRESS_BAR_Y, this.containerWidth,
				MenuSizes.PROGRESS_BAR_HEIGHT);

		JPanel panelProgressBar = new JPanel(new BorderLayout());

		panelProgressBar.setBounds(MenuSizes.MARGIN_LEFT, 0, this.containerWidth, MenuSizes.PROGRESS_BAR_HEIGHT);
		panelProgressBar.setAlignmentY(Component.CENTER_ALIGNMENT);
		panelProgressBar.setBackground(AppColors.getLightGrey());

		this.progressBar = new JProgressBar();
		this.progressBar.setBounds(MenuSizes.MARGIN_LEFT, 0, this.containerWidth, MenuSizes.PROGRESS_BAR_HEIGHT);

		panelProgressBar.add(this.progressBar);
		panel.add(panelProgressBar);
		return panel;
	}

	/**
	 * Sets the current image file.
	 *
	 * @param file
	 *            the image file.
	 *
	 */
	private void setCurrentImageWithMask(File file) {

		ImageIcon icon = getImageIconFromFile(file);

		if (this.currentImageMask == null) {
			if (icon != null) {
				this.currentImageMask = new JLabel(icon);
			}
		} else {
			if (icon != null) {
				this.currentImageMask.setIcon(icon);
			}
		}
	}

	private File getCurrentDisplayedImageWithMask(int x, int y, int width, int height) {
		try {

			BufferedImage rawIage = DeviceHelper.getDeviceRawImage();
			File currentImageWithMaskFile = Files.createTempFile(TMP_WITH_MASK, PNG).toFile();

			ImageIO.write(ImageHelper.addMask(rawIage, x, y, width, height, getCurrentSelectedColorMask()),
					FORMAT_NAME_PNG, currentImageWithMaskFile);

			currentImageWithMaskFile.deleteOnExit();

			return currentImageWithMaskFile;
		} catch (IOException e) {
			LOGGER.log(Level.OFF, e.getMessage(), e);
		}
		return null;
	}

	private File getCurrentDisplayedImageWithAllCreatedMask() {
		try {
			int maskCount = this.cbListMasks.getItemCount();

			BufferedImage rawIage = DeviceHelper.getDeviceRawImage();
			File currentImageWithMaskFile = Files.createTempFile(TMP_WITH_MASK, PNG).toFile();

			for (int i = 0; i < maskCount; i++) {
				Mask mask = this.cbListMasks.getItemAt(i);
				ImageIO.write(ImageHelper.addMask(rawIage, mask.getX(), mask.getY(), mask.getWidth(), mask.getHeight(),
						getCurrentSelectedColorMask()), FORMAT_NAME_PNG, currentImageWithMaskFile);
			}

			currentImageWithMaskFile.deleteOnExit();

			return currentImageWithMaskFile;
		} catch (IOException e) {
			LOGGER.log(Level.OFF, e.getMessage(), e);
		}
		return null;
	}

	private File getCurrentDisplayedImageWithAllMasks(int x, int y, int width, int height) {
		try {
			int maskCount = this.cbListMasks.getItemCount();

			BufferedImage rawIage = DeviceHelper.getDeviceRawImage();
			File currentImageWithMaskFile = Files.createTempFile(TMP_WITH_MASK, PNG).toFile();

			ImageIO.write(ImageHelper.addMask(rawIage, x, y, width, height, getCurrentSelectedColorMask()),
					FORMAT_NAME_PNG, currentImageWithMaskFile);

			for (int i = 0; i < maskCount; i++) {
				Mask mask = this.cbListMasks.getItemAt(i);
				ImageIO.write(ImageHelper.addMask(rawIage, mask.getX(), mask.getY(), mask.getWidth(), mask.getHeight(),
						getCurrentSelectedColorMask()), FORMAT_NAME_PNG, currentImageWithMaskFile);
			}

			currentImageWithMaskFile.deleteOnExit();

			return currentImageWithMaskFile;
		} catch (IOException e) {
			LOGGER.log(Level.OFF, e.getMessage(), e);
		}
		return null;
	}

	private File getCurrentDisplayedImageWithMaskEditValues() {
		int maskX = this.maskXTextField.getText().isEmpty() ? 0 : Integer.parseInt(this.maskXTextField.getText());
		int maskY = this.maskYTextField.getText().isEmpty() ? 0 : Integer.parseInt(this.maskYTextField.getText());
		int maskWidth = this.maskWidthTextField.getText().isEmpty() ? 0
				: Integer.parseInt(this.maskWidthTextField.getText());
		int maskHeight = this.maskHeightTextField.getText().isEmpty() ? 0
				: Integer.parseInt(this.maskHeightTextField.getText());

		return getCurrentDisplayedImageWithMask(maskX, maskY, maskWidth, maskHeight);
	}

	@Override
	public void caretUpdate(CaretEvent arg0) {
		updateDisplayedMask();
	}
}
