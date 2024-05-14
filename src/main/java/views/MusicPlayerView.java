package views;

import constants.ResourcesPath;
import controllers.MusicPlayerController;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import models.Song;
import styles.UIColor;
import styles.UISize;

public class MusicPlayerView extends JFrame {

    private MusicPlayerController musicPlayerController;

    // allow us to use file explorer in our app
    private JFileChooser jFileChooser;

    private JLabel songTitle, songArtist;
    private JPanel playbackBtns;
    private JSlider playbackSlider;
    JLabel songImage;
    JToolBar toolBar;
    JMenuBar menuBar;
    JMenu songMenu;
    JMenuItem createPlaylist;
    JMenuItem loadPlaylist;
    JMenuItem loadSong;
    JMenu playlistMenu;
    File selectedFile;
    // 5 icon
    ImageIcon prevButtonIcon, playButtonIcon, pauseButtonIcon, nextButtonIcon;
    public JButton prevButton, playButton, pauseButton, nextButton;

    public MusicPlayerView(){
        // calls JFrame constructor to configure out gui and set the title heaader to "Music Player"
        super("Music Player");

        // set the width and height
        setSize(UISize.MUSIC_PLAYER_WIDTH, UISize.MUSIC_PLAYER_HEIGHT);

        // end process when app is closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // launch the app at the center of the screen
        setLocationRelativeTo(null);

        // prevent the app from being resized
        setResizable(false);

        // set layout to null which allows us to control the (x, y) coordinates of our components
        // and also set the height and width
        setLayout(null);

        // change the frame color
        getContentPane().setBackground(UIColor.FRAME_COLOR);

        musicPlayerController = new MusicPlayerController(this);
        jFileChooser = new JFileChooser();

        // set a default path for file explorer
        jFileChooser.setCurrentDirectory(new File(ResourcesPath.CURRENT_DIRECTORY));

        // filter file chooser to only see .mp3 files
        jFileChooser.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));

        initIcon();
        addGuiComponents();
    }

    private void addGuiComponents(){
        // add toolbar
        addToolbar();

        // load record image
        songImage = new JLabel(loadImage(ResourcesPath.SONG_IMAGE));
        songImage.setBounds(0, 50, getWidth() - 20, 225);
        add(songImage);

        // song title
        songTitle = new JLabel("Song Title");
        songTitle.setBounds(0, 285, getWidth() - 10, 30);
        songTitle.setFont(new Font("Dialog", Font.BOLD, 24));
        songTitle.setForeground(UIColor.TEXT_COLOR);
        songTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(songTitle);

        // song artist
        songArtist = new JLabel("Artist");
        songArtist.setBounds(0, 315, getWidth() - 10, 30);
        songArtist.setFont(new Font("Dialog", Font.PLAIN, 24));
        songArtist.setForeground(UIColor.TEXT_COLOR);
        songArtist.setHorizontalAlignment(SwingConstants.CENTER);
        add(songArtist);

        // playback slider
        playbackSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        playbackSlider.setBounds(getWidth()/2 - 300/2, 365, 300, 40);
        playbackSlider.setBackground(null);
        playbackSlider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // when the user is holding the tick we want to the pause the song
                musicPlayerController.pauseSong();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // when the user drops the tick
                JSlider source = (JSlider) e.getSource();

                // get the frame value from where the user wants to playback to
                int frame = source.getValue();

                // update the current frame in the music player to this frame
                musicPlayerController.setCurrentFrame(frame);

                // update current time in milli as well
                musicPlayerController.setCurrentTimeInMilli((int) (frame / (2.08 * musicPlayerController.getCurrentSong().getFrameRatePerMilliseconds())));

                // resume the song
                musicPlayerController.playCurrentSong();

                // toggle on pause button and toggle off play button
                enablePauseButtonDisablePlayButton();
            }
        });
        add(playbackSlider);

        // playback buttons (i.e. previous, play, next)
        addPlaybackBtns();
    }

    private void addToolbar(){
        toolBar = new JToolBar();
        toolBar.setBounds(0, 0, getWidth(), 20);

        // prevent toolbar from being moved
        toolBar.setFloatable(false);

        // add drop down menu
        menuBar = new JMenuBar();
        toolBar.add(menuBar);

        // now we will add a song menu where we will place the loading song option
        songMenu = new JMenu("Song");
        menuBar.add(songMenu);

        // add the "load song" item in the songMenu
        loadSong = new JMenuItem("Load Song");
        loadSong.addActionListener(musicPlayerController);
        songMenu.add(loadSong);

        // now we will add the playlist menu
        playlistMenu = new JMenu("Playlist");
        menuBar.add(playlistMenu);

        // then add the items to the playlist menu
        createPlaylist = new JMenuItem("Create Playlist");
        createPlaylist.addActionListener(musicPlayerController);
        playlistMenu.add(createPlaylist);

        loadPlaylist = new JMenuItem("Load Playlist");
        loadPlaylist.addActionListener(musicPlayerController);
        playlistMenu.add(loadPlaylist);

        add(toolBar);
    }

    public JFileChooser getjFileChooser() {
        return jFileChooser;
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public void setSelectedFile(File selectedFile) {
        this.selectedFile = selectedFile;
    }

    private void initIcon(){
        prevButtonIcon = loadImage(ResourcesPath.PREVIOUS_BUTTON);
        playButtonIcon = loadImage(ResourcesPath.PLAY_BUTTON);
        pauseButtonIcon = loadImage(ResourcesPath.PAUSE_BUTTON);
        nextButtonIcon = loadImage(ResourcesPath.NEXT_BUTTON);
    }

    private void addPlaybackBtns(){
        playbackBtns = new JPanel();
        playbackBtns.setBounds(0, 435, getWidth() - 10, 80);
        playbackBtns.setBackground(null);

        // previous button
        prevButton = new JButton(prevButtonIcon);
        prevButton.setBorderPainted(false);
        prevButton.setBackground(null);
        prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        prevButton.addActionListener(musicPlayerController);
        playbackBtns.add(prevButton);

        // play button
        playButton = new JButton(playButtonIcon);
        playButton.setBorderPainted(false);
        playButton.setBackground(null);
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playButton.addActionListener(musicPlayerController);
        playbackBtns.add(playButton);

        // pause button
        pauseButton = new JButton(pauseButtonIcon);
        pauseButton.setBorderPainted(false);
        pauseButton.setBackground(null);
        pauseButton.setVisible(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.addActionListener(musicPlayerController);
        playbackBtns.add(pauseButton);

        // next button
        nextButton = new JButton(nextButtonIcon);
        nextButton.setBorderPainted(false);
        nextButton.setBackground(null);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(musicPlayerController);
        playbackBtns.add(nextButton);

        add(playbackBtns);
    }

    public Point getButtonLocation(JButton button){
        return button.getLocation();
    }

    // this will be used to update our slider from the music player class
    public void setPlaybackSliderValue(int frame){
        playbackSlider.setValue(frame);
    }

    public void updateSongTitleAndArtist(Song song){
        songTitle.setText(song.getSongTitle());
        songArtist.setText(song.getSongArtist());
    }

    public void updatePlaybackSlider(Song song){
        // update max count for slider
        playbackSlider.setMaximum(song.getMp3File().getFrameCount());

        // create the song length label
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();

        // beginning will be 00:00
        JLabel labelBeginning = new JLabel("00:00");
        labelBeginning.setFont(new Font("Dialog", Font.BOLD, 18));
        labelBeginning.setForeground(UIColor.TEXT_COLOR);

        // end will vary depending on the song
        JLabel labelEnd =  new JLabel(song.getSongLength());
        labelEnd.setFont(new Font("Dialog", Font.BOLD, 18));
        labelEnd.setForeground(UIColor.TEXT_COLOR);

        labelTable.put(0, labelBeginning);
        labelTable.put(song.getMp3File().getFrameCount(), labelEnd);

        playbackSlider.setLabelTable(labelTable);
        playbackSlider.setPaintLabels(true);
    }

    public void enablePauseButtonDisablePlayButton(){
        // retrieve reference to play button from playbackBtns panel
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        // turn off play button
        playButton.setVisible(false);
        playButton.setEnabled(false);

        // turn on pause button
        pauseButton.setVisible(true);
        pauseButton.setEnabled(true);
    }

    public void enablePlayButtonDisablePauseButton(){
        // retrieve reference to play button from playbackBtns panel
        JButton playButton = (JButton) playbackBtns.getComponent(1);
        JButton pauseButton = (JButton) playbackBtns.getComponent(2);

        // turn on play button
        playButton.setVisible(true);
        playButton.setEnabled(true);

        // turn off pause button
        pauseButton.setVisible(false);
        pauseButton.setEnabled(false);
    }

    public ImageIcon loadImage(String imagePath){
        try{
            // read the image file from the given path
            BufferedImage image = ImageIO.read(getClass().getResource(imagePath));

            // returns an image icon so that our component can render the image
            return new ImageIcon(image);
        }catch(Exception e){
            System.out.println(e.getMessage() + " " + imagePath);
        }

        // could not find resource
        return null;
    }
}









