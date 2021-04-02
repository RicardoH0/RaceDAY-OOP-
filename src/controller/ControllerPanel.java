
package controller;

import static model.PropertyChangeEnabledRaceControls.*;

import application.Main;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileSystemView;
import model.PropertyChangeEnabledRaceControls;
import model.RaceModel;
import model.info.RaceInfo;
import model.info.RaceParticipant;
import model.messages.Message;
import view.Utilities;

/**
 * Comment me.
 * 
 * @author Charles Bryan
 * @author Ricardo Ho
 * @version Autumn 2019
 */
public class ControllerPanel extends JPanel {

    /** The separator for formatted. */
    public static final String SEPARATOR = ":";

    /** The number of milliseconds in a second. */
    public static final int MILLIS_PER_SEC = 1000;

    /** The number of seconds in a minute. */
    public static final int SEC_PER_MIN = 60;

    /** The number of minute in a hour. */
    public static final int MIN_PER_HOUR = 60;

    /** A formatter to require at least 1 digit, leading 0. */
    public static final DecimalFormat ONE_DIGIT_FORMAT = new DecimalFormat("0");

    /** A formatter to require at least 2 digits, leading 0s. */
    public static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");

    /** A formatter to require at least 3 digits, leading 0s. */
    public static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat("000");

    /** The serialization ID. */
    private static final long serialVersionUID = -6759410572845422202L;

    /** Start text for the start/stop button. */
    private static final String BUTTON_ICON_START = "./images/ic_play.png";

    /** Stop text for the start/stop button. */
    private static final String BUTTON_ICON_STOP = "./images/ic_pause.png";

    /** Start text for the start/stop button. */
    private static final String BUTTON_TEXT_START = "Play";

    /** Stop text for the start/stop button. */
    private static final String BUTTON_TEXT_STOP = "Pause";

    /** Start text for the start/stop button. */
    private static final String BUTTON_ICON_REGULAR = "./images/ic_one_times.png";

    /** Stop text for the start/stop button. */
    private static final String BUTTON_ICON_FAST = "./images/ic_four_times.png";

    /** Start text for the speed button. */
    private static final String BUTTON_TEXT_REGULAR = "Regular";

    /** Stop text for the speed button. */
    private static final String BUTTON_TEXT_FAST = "Fast";

    /** Amount of milliseconds between each call to the timer. */
    private static final int TIMER_FREQUENCY = 31;

    /** Value for regular multiplier. */
    private static final int SPEED_REGULAR = 1;

    /** Value for fast multiplier. */
    private static final int SPEED_FAST = 4;

    /** A reference to the backing Race Model. */
    private final PropertyChangeEnabledRaceControls myRace;

    /** Display of messages coming from the Race Model. */
    private final JTextArea myOutputArea;

    /** Panel to display CheckBoxs for each race Participant. */
    private final JPanel myParticipantPanel;

    /** A view on the race model that displays the current race time. */
    private final JLabel myTimeLabel;

    /** A controller and view of the Race Model. */
    private final JSlider myTimeSlider;

    /** The list of javax.swing.Actions that make up the ToolBar (Controls) buttons. */
    private final List<Action> myControlActions;

    /** The timer that advances the Race Model. */
    private final Timer myTimer;

    /** The delta value for time change. */
    private int myDelta;

    /** Container to hold the different output areas. */
    private final JTabbedPane myTabbedPane;

    /** */
    private boolean isSelected = false;

    /**
     * Construct a ControllerPanel.
     * 
     * @param theRace the backing race model
     */
    public ControllerPanel(final PropertyChangeEnabledRaceControls theRace) {
        super();
        myOutputArea = new JTextArea(10, 50);
        myTimeLabel = new JLabel(Utilities.formatTime(0));
        myRace = theRace;

        myTimeSlider = new JSlider(0, 0, 0);
        myControlActions = new ArrayList<>();

        myTabbedPane = new JTabbedPane();
        myParticipantPanel = new JPanel();
        // TODO This component requires Event Handler(s) CHECKED
        myTimer = new Timer(TIMER_FREQUENCY, this::handleTimerTicks);
        myDelta = SPEED_REGULAR;

        buildActions();
        setUpComponents();
    }

    /**
     * Displays a simple JFrame.
     */
    private void setUpComponents() {
        setLayout(new BorderLayout());

        // JPanel is a useful container for organizing components inside a JFrame
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(buildSliderPanel(), BorderLayout.NORTH);

        myOutputArea.setEditable(false);

        final JScrollPane scrollPane = new JScrollPane(myOutputArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        final JScrollPane participantScrollPane = new JScrollPane(myParticipantPanel);
        participantScrollPane.setPreferredSize(scrollPane.getSize());

        myTabbedPane.addTab("Data Output Stream", scrollPane);
        myTabbedPane.addTab("Race Participants", participantScrollPane);

        mainPanel.add(myTabbedPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        add(buildToolBar(), BorderLayout.SOUTH);

    }

    /**
     * Builds the panel with the time slider and time label.
     * 
     * @return the panel
     */
    private JPanel buildSliderPanel() {
        // TODO These components require Event Handlers
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 25, 5));

        myTimeSlider.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panel.add(myTimeSlider, BorderLayout.CENTER);

        myTimeLabel.setBorder(BorderFactory
                        .createCompoundBorder(BorderFactory.createEtchedBorder(),
                                              BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        // Time Label
        class TimeUpdate implements PropertyChangeListener {

            @Override
            public void propertyChange(final PropertyChangeEvent theEvent) {
                if (PROPERTY_TIME.equals(theEvent.getPropertyName())) {
                    myTimeLabel.setText(formatTime((Integer) theEvent.getNewValue()));
                    myTimeSlider.setValue((Integer) theEvent.getNewValue());
                    myTimeSlider.addChangeListener(new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {
                            JSlider slider = (JSlider) e.getSource();
                            final int time = slider.getValue();
                            System.out.println(time);
                            myRace.moveTo(time);
                            
                        }
                        
                    });
                }

            }

        }
        myRace.addPropertyChangeListener(new TimeUpdate());

        myRace.addPropertyChangeListener(PROPERTY_RACE_INFORMATION,
                                         new PropertyChangeListener() {

            @Override
            public void propertyChange(final PropertyChangeEvent theEvent) {
            
                final RaceInfo tempInfo = (RaceInfo) theEvent
                                .getNewValue();
                myTimeSlider.setMaximum(tempInfo.getTotalTime());
                myTimeSlider.setMajorTickSpacing(tempInfo.getTotalTime()/10);
                myTimeSlider.setMinorTickSpacing(tempInfo.getTotalTime()/100);
                myTimeSlider.setPaintTicks(true);
            }

        });
        
        
//        myTimeSlider.addChangeListener(new ChangeListener() {
//
//            @Override
//            public void stateChanged(ChangeEvent e) {
//                JSlider slider = (JSlider) e.getSource();
//                final int time = slider.getValue();
//                System.out.println(time);
//                myRace.moveTo(time);
//                
//            }
//            
//        });
        final JPanel padding = new JPanel();
        padding.add(myTimeLabel);
        panel.add(padding, BorderLayout.EAST);

        return panel;
    }

    /**
     * This formats a positive integer into minutes, seconds, and milliseconds. 00:00:000
     * 
     * @param theTime the time to be formatted
     * @return the formated string.
     */
    private static String formatTime(final long theTime) {
        long time = theTime;
        final long milliseconds = time % MILLIS_PER_SEC;
        time /= MILLIS_PER_SEC;
        final long seconds = time % SEC_PER_MIN;
        time /= SEC_PER_MIN;
        final long min = time % MIN_PER_HOUR;
        time /= MIN_PER_HOUR;
        return TWO_DIGIT_FORMAT.format(min) + SEPARATOR + TWO_DIGIT_FORMAT.format(seconds)
               + SEPARATOR + THREE_DIGIT_FORMAT.format(milliseconds);
    }

    /**
     * Constructs a JMenuBar for the Frame.
     * 
     * @return the Menu Bar
     */
    private JMenuBar buildMenuBar() {
        final JMenuBar bar = new JMenuBar();
        bar.add(buildFileMenu());
        bar.add(buildControlsMenu(myControlActions));
        bar.add(buildHelpMenu());
        return bar;
    }

    /**
     * Builds the file menu for the menu bar.
     * 
     * @return the File menu
     */
    private JMenu buildFileMenu() {
        // TODO These components require Event Handlers

        final JMenu fileMenu = new JMenu("File");

        final JMenuItem load = new JMenuItem("Load Race...");

        fileMenu.add(load);

        // Load File
        load.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {
                final JFileChooser j =
                                new JFileChooser(new File(System.getProperty("user.dir")));
                final int r = j.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {

                    try {
                        final File file = j.getSelectedFile();
                        myRace.loadRace(file);
                        for (final Action a : myControlActions) {
                            a.setEnabled(true);
                        }
                    } catch (final IOException exception) {
                        JOptionPane.showMessageDialog(null, "Error Format File!");
                    }
                }
            }
        });

        // Message update
        class MessageUpdate implements PropertyChangeListener {

            @Override
            public void propertyChange(final PropertyChangeEvent theEvent) {
                if (PROPERTY_MESSAGE.equals(theEvent.getPropertyName())) {
                    final Message message = (Message) theEvent.getNewValue();
                    myOutputArea.append(message.getMessage() + "\n");
                }
                if (PROPERTY_STRING_MESSAGE.equals(theEvent.getPropertyName())) {
                    final String status = (String) theEvent.getNewValue();
                    myOutputArea.append(status + "\n");
                }
                if (PROPERTY_RACE_INFORMATION.equals(theEvent.getPropertyName())) {
                    final RaceInfo tempInfo = (RaceInfo) theEvent.getNewValue();
                    final List<RaceParticipant> racers = tempInfo.getParticipants();
                    for (int i = 0; i < racers.size(); i++) {
                        final String tempName = racers.get(i).getName();
                        final JLabel name = new JLabel();
                        name.setText(tempName);
                        myParticipantPanel.setLayout(new BoxLayout(myParticipantPanel,
                                                                   BoxLayout.Y_AXIS));
                        myParticipantPanel.add(name);
                        myParticipantPanel.setEnabled(false);
                    }
                }
            }
        }
        myRace.addPropertyChangeListener(new MessageUpdate());

        fileMenu.addSeparator();

        final JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(exitItem);
        return fileMenu;
    }

    /**
     * Build the Controls JMenu.
     * 
     * @param theActions the Actions needed to add/create the items in this menu
     * @return the Controls JMenu
     */
    private JMenu buildControlsMenu(final List<Action> theActions) {
        final JMenu controlsMenu = new JMenu("Controls");

        for (final Action a : theActions) {
            controlsMenu.add(a);
            a.setEnabled(false);
        }
        // controlsMenu.getComponent().setEnabled(false);

        return controlsMenu;
    }

    /**
     * Build the Help JMenu.
     * 
     * @return the Help JMenu
     */
    private JMenu buildHelpMenu() {
        // TODO These components require Event Handlers
        final JMenu helpMenu = new JMenu("Help");

        final JMenuItem infoItem = new JMenuItem("Race Info...");
        helpMenu.add(infoItem);
        infoItem.setEnabled(false);
        final JPanel panel = new JPanel();

        final JLabel raceName = new JLabel();
        final JLabel trackType = new JLabel();
        final JLabel totalTime = new JLabel();
        final JLabel lapDint = new JLabel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(raceName);
        panel.add(trackType);
        panel.add(totalTime);
        panel.add(lapDint);

        class RaceInfoUpdate implements PropertyChangeListener {

            @Override
            public void propertyChange(final PropertyChangeEvent theEvent) {
                if (PROPERTY_RACE_INFORMATION.equals(theEvent.getPropertyName())) {
                    infoItem.setEnabled(true);
                    final RaceInfo tempInfo = (RaceInfo) theEvent.getNewValue();
                    raceName.setText(tempInfo.getRaceName());
                    trackType.setText("Track type: " + tempInfo.getTrackType());
                    totalTime.setText("Total Time: " + formatTime(tempInfo.getTotalTime()));
                    lapDint.setText("Lap Distance: "
                                    + String.valueOf(tempInfo.getTrackDistance()));

                }

            }

        }
        myRace.addPropertyChangeListener(new RaceInfoUpdate());

        infoItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent theEvent) {

                JOptionPane.showMessageDialog(null, panel, "Race Info",
                                              JOptionPane.PLAIN_MESSAGE);

            }
        });

        final JMenuItem aboutItem = new JMenuItem("About...");
        helpMenu.add(aboutItem);
        return helpMenu;
    }

    /**
     * Build the toolbar from the Actions list.
     * 
     * @return the toolbar with buttons for all of the Actions in the list
     */
    private JToolBar buildToolBar() {
        final JToolBar toolBar = new JToolBar();
        for (final Action a : myControlActions) {
            final JButton b = new JButton(a);
            b.setHideActionText(true);
            toolBar.add(b);

        }

        return toolBar;
    }

    /**
     * Add actionListeners to the buttons.
     */
    private void addListeners() {
        buildActions();

    }


    // // TESTING
    // class MySliderHandler implements ChangeListener {
    //
    // @Override
    // public void stateChanged(ChangeEvent theEvent) {
    // JSlider source = (JSlider) theEvent.getSource();
    // if (!source.getValueIsAdjusting()) {
    // int sec = (int) source.getValue();
    // myRace.moveTo(sec);
    // }
    // }
    // }
    // myTimeSlider.addChangeListener(new MySliderHandler());

    // Timer Ticks Handler
    private void handleTimerTicks(final ActionEvent theEvent) {
        myRace.advance(TIMER_FREQUENCY * myDelta);
    }

    /**
     * Instantiate and add the Actions.
     */
    private void buildActions() {
        // TODO These components require Event Handlers
        myControlActions.add(new RestartAction("Restart", "/ic_restart.png"));
        myControlActions.add(new StartAction("Play", "/ic_play.png"));
        myControlActions.add(new SpeedAction("Times One", "/ic_one_times.png"));
        myControlActions.add(new RepeatAction("Single Race", "/ic_repeat.png"));
        myControlActions.add(new CleanAction("Clear", "/ic_clear.png"));

    }

    /**
     * Create the GUI and show it. For thread safety, this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        // Create and set up the window.
        final JFrame frame = new JFrame("Astonishing Race!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // TODO instantiate your model here.
        final PropertyChangeEnabledRaceControls race = new RaceModel();

        // Create and set up the content pane.
        final ControllerPanel pane = new ControllerPanel(race);

        // Add the JMenuBar to the frame:
        frame.setJMenuBar(pane.buildMenuBar());

        pane.setOpaque(true); // content panes must be opaque
        frame.setContentPane(pane);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * This is a simple implementation of an Action. You will most likely not use this
     * implementation in your final solution. Either create your own Actions or alter this to
     * suit the requirements for this assignment.
     * 
     * @author Charles Bryan
     * @version Autumn 2019
     */
    private class SimpleAction extends AbstractAction {

        /** The serialization ID. */
        private static final long serialVersionUID = -3160383376683650991L;

        /**
         * Constructs a SimpleAction.
         * 
         * @param theText the text to display on this Action
         * @param theIcon the icon to display on this Action
         */
        SimpleAction(final String theText, final String theIcon) {
            super(theText);

            // small icons are usually assigned to the menu
            ImageIcon icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image smallImage = icon.getImage()
                            .getScaledInstance(16, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);

            // Here is how to assign a larger icon to the tool bar.
            icon = (ImageIcon) new ImageIcon(getRes(theIcon));
            final Image largeImage = icon.getImage()
                            .getScaledInstance(24, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
        }

        /**
         * Wrapper method to get a system resource.
         * 
         * @param theResource the name of the resource to retrieve
         * @return the resource
         */
        private URL getRes(final String theResource) {
            return Main.class.getResource(theResource);
        }

        protected void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage = icon.getImage()
                            .getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);

            final Image smallImage = icon.getImage()
                            .getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            // TODO If you use this Action class, your behaviors go here.

        }
    }

    private class StartAction extends SimpleAction {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        StartAction(final String theText, final String theIcon) {
            super(theText, theIcon);
            // TODO Auto-generated constructor stub
        }

        public void actionPerformed(final ActionEvent theEvent) {
            if (myTimer.isRunning()) {

                myTimer.stop();
                putValue(Action.NAME, BUTTON_TEXT_START);
                setIcon(new ImageIcon(BUTTON_ICON_START));
            } else {
                myTimer.start();
                putValue(Action.NAME, BUTTON_TEXT_STOP);
                setIcon(new ImageIcon(BUTTON_ICON_STOP));
            }
        }
    }

    private class RestartAction extends SimpleAction {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        RestartAction(final String theText, final String theIcon) {
            super(theText, theIcon);
            // TODO Auto-generated constructor stub
        }

        public void actionPerformed(final ActionEvent theEvent) {
            myRace.moveTo(0);
        }
    }

    private class SpeedAction extends SimpleAction {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        SpeedAction(final String theText, final String theIcon) {
            super(theText, theIcon);
            // TODO Auto-generated constructor stub
        }

        public void actionPerformed(final ActionEvent theEvent) {
            if (myDelta == SPEED_REGULAR) {
                myDelta = SPEED_FAST;
                putValue(Action.NAME, BUTTON_TEXT_FAST);
                setIcon(new ImageIcon(BUTTON_ICON_FAST));
            } else {
                myDelta = SPEED_REGULAR;
                putValue(Action.NAME, BUTTON_TEXT_REGULAR);
                setIcon(new ImageIcon(BUTTON_ICON_REGULAR));
            }
        }
    }

    private class RepeatAction extends SimpleAction {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        RepeatAction(final String theText, final String theIcon) {
            super(theText, theIcon);
            // TODO Auto-generated constructor stub
        }

        public void actionPerformed(final ActionEvent theEvent) {

            if (isSelected == true) {
                putValue(Action.NAME, "Single Race");
                setIcon(new ImageIcon("./images/ic_repeat.png"));
                myRace.addPropertyChangeListener(PROPERTY_RACE_STATUS,
                                                 new PropertyChangeListener() {
                @Override
                public void propertyChange(final PropertyChangeEvent theEvent) {
                    if (theEvent.getNewValue().equals(false)) {
                        myTimer.stop();
                    }                    
                }   
                });
                isSelected = false;
                
            } else {
                putValue(Action.NAME, "Loop Race");
                setIcon(new ImageIcon("./images/ic_repeat_color.png"));
                myRace.addPropertyChangeListener(PROPERTY_RACE_STATUS,
                                                 new PropertyChangeListener() {
                @Override
                public void propertyChange(final PropertyChangeEvent theEvent) {
                    if (theEvent.getNewValue().equals(false)) {
                        myRace.moveTo(0);
                    }                    
                }   
                });
                isSelected = true;
            }
        }
    }

    private class CleanAction extends SimpleAction {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        CleanAction(final String theText, final String theIcon) {
            super(theText, theIcon);
            // TODO Auto-generated constructor stub
        }

        public void actionPerformed(final ActionEvent theEvent) {
            myOutputArea.selectAll();
            myOutputArea.replaceSelection("");
        }
    }

}


