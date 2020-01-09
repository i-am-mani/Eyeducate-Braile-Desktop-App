package sample;

import java.io.*;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

import java.util.StringTokenizer;


import com.fazecast.jSerialComm.SerialPort;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.effect.Blend;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BrailleController {

    Thread currentStepsThread;

    //-->processes<--//
    Process processTextToSpeechByArgv = null;

    //-->temp(TextToBraille)<--//
    @FXML
    private Button btnUploadTxtToBraille;

    @FXML
    private TextField txtToBrailleDelayField;

    @FXML
    private TextField pdfToBrailleDelayField;
    @FXML
    private Button BtnUploadPdfToBraille;

    @FXML
    private Label textLabelTextToBraille;
    @FXML
    private Label BrailleLabelTextToBraille;
    @FXML
    private Line vertLineTextToBraille;


    //===>Confirmation labels<====//
    @FXML
    private Label savePdfConfirmLabel;
    @FXML
    private Circle circleConfirmSpeech;
    @FXML
    private Circle lightModeCircle;
    @FXML
    private Circle darkModeCircle;

    //------>Constants<--------//

    private String DEFAULT_SRC_DIR = "E";
    private int SCENE_ON_FRONT = 0; //Uses 0:anchPdf,1:anchText,2:anchCustomize,
    // 3:anchCommandPromt,4:anchLearning,5:anchDictionary,
    // 6:anchNews,7:anchWikipedia,8:OCR,9:Quiz.
    private int i = 0;
    private Event EVENT;
    private int GOTO_PAGE = 0;

    //-->Code for SpeechToTextLib<--//
    SpeechToTextLib speechToTextLib = new SpeechToTextLib();


    //-->Code for Text to Speech<--//
    Task<Void> taskRead = null;
    Process p;
    @FXML
    CheckBox readOnChangeChbx;

    //--->Customization Elements<---//
    @FXML
    private ColorPicker backgroundColor;
    @FXML
    private ColorPicker previewColor;
    @FXML
    private ColorPicker textColor;
    @FXML
    private ColorPicker buttonColor;
    @FXML
    private ColorPicker borderColor;
    @FXML
    private Button applyCustomizeColor;
    @FXML
    private Label tSerialMode;
    @FXML
    private Button btnSetSerial;
    @FXML
    private ChoiceBox<SerialPort> serialPortChoice;
    ObservableList<SerialPort> serialPortList = FXCollections.observableArrayList();

    //-->Learning mode<--//
    @FXML
    private Button btnLearningStart;
    @FXML
    private Button btnLearningTest;
    @FXML
    private TextField timeIntervalField;
    @FXML
    private Label tLearn;
    @FXML
    private Label tLearnInfo1;
    @FXML
    private Label tLearnInfo2;
    @FXML
    private Label tTest;
    @FXML
    private Label tTestInfo;
    @FXML
    private Label tDelay;

    @FXML
    private Line lVert1;
    @FXML
    private Line lHor1;
    @FXML
    private Line lVert2;
    @FXML
    private Line lVert3;
    @FXML
    private Label tGoto;
    @FXML
    private TextField learnGoToField;
    @FXML
    private Button btnLearnGo;

    String str = "abcdefghijklmnopqrstuvwxyz0123456789";
    @FXML
    private Button btnLearningRestart;
    @FXML
    private Label testValidationLabel;

    private Process processForArduinoStepper;

    private SerialPort comPort = null;
    private OutputStream outputStreamForSerial;
    private InputStream inputStreamForSerial;

    private boolean restartRotation = false;
    private boolean isStepperAlive = true;
    private Thread threadRotateToChar;
    private boolean isSerialAlive = false;


    //-->Anchor panes<--//
    @FXML
    AnchorPane anchPdf;
    @FXML
    AnchorPane anchText;
    @FXML
    AnchorPane anchMain;
    @FXML
    private AnchorPane anchCustomize;
    @FXML
    private AnchorPane anchLearningMode;
    @FXML
    private AnchorPane anchCommandPromt;
    @FXML
    private AnchorPane anchDictonary;
    @FXML
    private AnchorPane anchWikipedia;
    @FXML
    private AnchorPane anchNews;


    //--->Command Promt<---//
    @FXML
    private TextArea commandPromtPreview;
    @FXML
    private CheckBox keyboardChbx;


    //-->learning mode<---//
    @FXML
    private Button btnLearningMode;


    @FXML
    private Button btnNextpage;
    @FXML
    private Button btnGoTo;
    @FXML
    private TextField goToField;

    @FXML
    private Text tEquals;

    @FXML
    private Button btnPrint;

    @FXML
    private Button btnPrevpage;

    @FXML
    private Text textPreview;
    @FXML
    private Button btnPdfToBraille;

    @FXML
    private Button btnTextToBraille;

    @FXML
    private Button btnCustom;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnOutputfile;

    @FXML
    private Button btnPdfSource;
    @FXML
    private Button btnPdfDestination;

    @FXML
    private Label tCustomize;
    @FXML
    private Label tColorScheme;
    @FXML
    private Label tPreview;
    @FXML
    private Label tBackground;
    @FXML
    private Label tText;
    @FXML
    private Label tButton;
    @FXML
    private Label tBorder;

    private int pageNo = 0;

    @FXML
    CheckBox rangeCheckBox;
    @FXML
    TextField rangeFrom;
    @FXML
    TextField rangeTo;

    @FXML
    TextField destField;

    @FXML
    TextArea writtenTextArea;

    @FXML
    TextArea brailleTextArea;

    @FXML
    Text outputTextField;

    @FXML
    Button outputBtn;

    @FXML
    Button closeBtn;

    @FXML
    Button minimizeBtn;
    Stage stage;
    brailleConverter converter = new brailleConverter();

    String src = "E:\\Java books\\computer-networks-a-systems-approach-larry-l-peterson-morgan-kaufmann.pdf";

    String dst;


    //Decelerations for anchPdf
    @FXML
    TextArea braillePdfTextArea;
    @FXML
    Text sourceText;
    @FXML
    TextField destTextField;

    String pdfName;
    private int flag;
    private boolean isReaderOn = false;

    //-->Declerations for dictionary<--//
    @FXML
    public Label tDictionaryTitle;
    @FXML
    public Label tSearch;
    @FXML
    public Label tDefination;
    @FXML
    public Label tSynonyms;
    @FXML
    public Label tAntonyms;
    @FXML
    public TextField searchWordField;
    @FXML
    public Button btnSearchWord;
    @FXML
    public TextArea definitionTextArea;
    @FXML
    public TextArea synonymsTextArea;
    @FXML
    public TextArea antonymsTextArea;
    @FXML
    public Button btnReadDefinition;
    @FXML
    public Button btnUploadDefinitionToBraille;
    @FXML
    public Button btnDictionaryToBraille;
    @FXML
    public Button btnStopReadingDictionary;
    @FXML
    public Line line1;
    @FXML
    public Line line2;
    @FXML
    public Line line3;

    private SearchDictionary dictionary = new SearchDictionary();

    @FXML
    public Button btnSpellcheck;

    @FXML
    private ComboBox comboBox;

    ArrayList<String> spellcheckArray = new ArrayList<>();

    int curSpell = 0;
    boolean isSpellcheckMode = false;


    //-->declarations for wikipedia<--//
    @FXML
    public Button btnWikipediaToBraille;
    @FXML
    public Line wLine1;
    @FXML
    public Line wLine2;
    @FXML
    public Line wLine3;
    @FXML
    public Label tWikipediaTitle;
    @FXML
    public Label tSearchWikipedia;
    @FXML
    public TextField searchWikipediaField;
    @FXML
    public TextArea wikipediaTextArea;
    @FXML
    public Button btnSearchWikipedia;
    @FXML
    public Button btnReadWikipedia;
    @FXML
    public Button btnUploadWikipedia;

    webScrapper scrapper = new webScrapper();
    @FXML
    public Button btnStopReadingWikipedia;

    //-->>>Declarations for News<<<--//
    @FXML
    public Button btnNewsToBraille;
    @FXML
    public ListView<String> listView;
    ObservableList<String> headlines = FXCollections.observableArrayList();
    @FXML
    public TextArea articleTextArea;
    private int headlinePageNumber = 1;
    @FXML
    public Button btnNextNews;
    @FXML
    public Button btnPrevNews;
    @FXML
    public Label tNewsTitle;
    private String reply;

    @FXML
    public Button btnReadArticle;
    @FXML
    public Button btnReadHeadlines;

    int currentHeadline = 0;
    @FXML
    public Button btnStopReadingNews;
    Thread processReadHeadlines = null;

    int stopReadingFlag = 0;
    private String currentTheme;
    private boolean textToAudioFlag = true;


    //--->>>OCR declaration<<--//
    @FXML
    public AnchorPane anchOcr;
    @FXML
    public Label tImageSource;
    @FXML
    public TextField imageSourceField;
    @FXML
    public Button btnSourceImage;
    @FXML
    public ImageView sourceImageView;
    @FXML
    public TextArea ocrTextArea;
    @FXML
    public Button btnOcrRead;
    @FXML
    public Button btnOcrUpload;
    String imageSourcePath;

    @FXML
    public Button btnOcrMode;

    //-->>>Quiz Mode Decleration<<<--//
    @FXML
    public AnchorPane anchQuiz;
    @FXML
    public Label tQuizMode;
    @FXML
    public Label tProblemStatement;
    @FXML
    public Label tAnswer;
    @FXML
    public Label tCorrection;
    @FXML
    public Button btnNextQuiz;
    @FXML
    public Button btnPrevQuiz;
    @FXML
    public Button btnSubmitQuiz;
    @FXML
    public Button btnResetQuiz;
    @FXML
    public TextArea quizTextArea;
    @FXML
    public TextField quizAnswerField;
    ArrayList<String> questionList = new ArrayList<>();
    int resetQuestionsFlag = 1;
    int questionNumber = 0;
    String answer;
    @FXML
    public Label tQuetionNumber;

    @FXML
    public Button btnQuizMode;
    @FXML
    public Line qLine1;
    @FXML
    public Line qLine2;
    @FXML
    public Line qLine3;
    @FXML
    public Line qLine4;
    private int currentStepsThreadFlag = 0;

    //-->>>serial port Selection<<<--//
    public void showSerialAvailable(Event e) {

        serialPortList.clear(); //Clear past serial port records

        SerialPort[] comPort = SerialPort.getCommPorts();

        for (SerialPort i : comPort) {
            serialPortList.add(i); //update the list with new ports
            System.out.println(i.toString());
        }
        serialPortChoice.setItems(serialPortList);
    }

    public void selectSerialPort(Event e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                comPort = serialPortChoice.getValue();
                System.out.println("comport selected " + comPort.isOpen());
                if (!comPort.isOpen()) {
                    comPort.openPort();
                    comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
                    inputStreamForSerial = comPort.getInputStream();
                    outputStreamForSerial = comPort.getOutputStream();
                    System.out.println(comPort.isOpen());
                    System.out.println(inputStreamForSerial);
                    isSerialAlive = comPort.isOpen();
                    serialPortChoice.setValue(comPort);

                    textToSpeechByArgv("Port Selected");
                }
            }
        }).start();
    }

    //-->>>Quiz methods<<<--//

    @FXML
    public void initialize() {

        changeThemeDarcula(null);
    }

    public void nextQuiz(Event e) {
        String reply;
        quizAnswerField.clear();
        if (resetQuestionsFlag == 1) {
            try {
                BufferedReader in = new BufferedReader(new FileReader("qaset.txt"));
                while ((reply = in.readLine()) != null) {
                    questionList.add(reply);
                }
                resetQuestionsFlag = 0;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        String[] arr = questionList.get(questionNumber).split("\\|");
        String qNumber = arr[0];
        String question = arr[1];
        answer = arr[2];
        tQuetionNumber.setText("Q." + qNumber + ")");

        quizTextArea.setText(question);
        questionNumber++;
    }

    public void submitQuiz(Event e) {

        if (quizAnswerField.getText().equals(answer)) {
            tCorrection.setTextFill(Color.GREEN);
            tCorrection.setText("Correct");
            textToSpeechByArgv("Correct Answer");
        } else if (quizAnswerField.getText().equals(answer)) {
            tCorrection.setTextFill(Color.RED);
            tCorrection.setText("Wrong");
            textToSpeechByArgv("Wrong Answer");
        } else if (quizAnswerField.getText().equals("")) {
            textToSpeechByArgv("Empty Field");

        }
    }

    public void prevQuiz(Event e) {
        quizAnswerField.clear();
        String reply;
        if (resetQuestionsFlag == 1) {
            try {
                BufferedReader in = new BufferedReader(new FileReader("qaset.txt"));
                while ((reply = in.readLine()) != null) {
                    questionList.add(reply);
                }
                resetQuestionsFlag = 0;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        questionNumber--;
        String[] arr = questionList.get(questionNumber - 1).split("\\|");
        String qNumber = arr[0];
        String question = arr[1];
        answer = arr[2];
        tQuetionNumber.setText("Q." + qNumber + ")");

        quizTextArea.setText(question);

    }

    public void resetQuestionsQuiz(Event e) {
        resetQuestionsFlag = 1;
    }


    public void readQuestion(Event e) {
        String[] arr = questionList.get(questionNumber - 1).split("\\|");
        String qNumber = arr[0];
        String question = arr[1];

        String str = "Question number " + qNumber + "," + question;
        textToSpeechByArgv(str);
    }

    //-->>>Methods for OCR Scene<<<--//

    public void selectImageFile(Event e) {
        FileChooser imageChooser = new FileChooser();
        FileChooser.ExtensionFilter imageExtension = new FileChooser.ExtensionFilter("Images", "*.jpg", "*bmp");
        imageChooser.setSelectedExtensionFilter(imageExtension);
        File choosenFile = imageChooser.showOpenDialog(stage);
        imageSourcePath = choosenFile.getPath();
        System.out.println(imageSourcePath);
        Image image = new Image("file:" + imageSourcePath);
        sourceImageView.setImage(image);

        imageSourceField.setText(imageSourcePath);

        fetchText(e);
    }

    public void fetchText(Event k) {
        String path = new File(".").getAbsolutePath();
        fetchContent(path);
    }

    private void fetchContent(String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("python " + path.replace(".", "ocr.py") + " " + "\"" + imageSourcePath + "\"");
                try {
                    Process p = Runtime.getRuntime().exec("python " + path.replace(".", "ocr.py") + " " + "\"" + imageSourcePath + "\"");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    String reply = bufferedReader.readLine();
                    System.out.println(reply);
                    ocrTextArea.setText(reply);
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }


    public void readTextFromImage() {
        String str = ocrTextArea.getText();
        textToSpeechByArgv(str);
    }

    ArrayList<String> addressList = new ArrayList<>();

    public void readAllImagesDir() {
        File dir = new File("E");
        for (File f : dir.listFiles()) {
            if (f.toString().endsWith(".jpg")) {
//                addressList.add(f.toPath().toString());
                String path = f.toPath().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        System.out.println("python " + path.replace(".", "ocr.py") + " " + "\"" + imageSourcePath + "\"");
                        try {
                            Process p = Runtime.getRuntime().exec("python " + path.replace(".", "ocr.py") + " " + "\"" + imageSourcePath + "\"");
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                            String reply = bufferedReader.readLine();
                            System.out.println(reply);
                            ocrTextArea.setText(reply);
                            bufferedReader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();


            }
        }
    }


    /**
     * Creates drop shadow effect of Node that calls the method.
     */
    public void dropShadowEffectOn(Event e) {

        if (currentTheme.equals("light")) {

            Effect effect = new DropShadow(10, Color.web("#414142"));
            ((Button) e.getSource()).setEffect(effect);
        } else if (currentTheme.equals("dark")) {
            Effect effect = new DropShadow(10, Color.web("#8db7dd"));
            ((Button) e.getSource()).setEffect(effect);
        }
    }

    /**
     * Removes the drop shadow effect
     *
     * @param e
     */
    public void dropShadowEffectOff(Event e) {

        Effect effect = new Blend();
        ((Button) e.getSource()).setEffect(effect);
    }


    /**
     * Populates the Headlines ListView,and increments the headline's page number by 1
     *
     * @param e
     */
    public void populateListViewNextPage(Event e) {
        listView.setItems(headlines);
        headlines.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {
                scrapper.extractTopHeadlines(headlinePageNumber);

                for (String str : webScrapper.topHeadlines.keySet()) {
                    headlines.add(str);
                }
            }
        }).start();

        headlinePageNumber++;


    }

    /**
     * Reads the content of the  articleTextArea.
     *
     * @param e
     */
    public void readArticle(Event e) {
        /*
            Sends the text to read out i.e the Text inside the articleTextArea to
            textToAudioSysArgv.py as Command line argument and python file reads out the
            Content/text
         */
        stopNewsToAudio(e);
        String content = articleTextArea.getText();
        if (content != "") {
            textToSpeechByArgv(content);
        }

    }

    /**
     * Reads content of the Listview which has the headlines.
     *
     * @param e
     */
    public void readHeadlines(Event e) {
        /*
               Implementation Note:- each headline is passed to be read,and headline number is
               incremented only when past headline is read. We do this by checking if the process which
               executes the python file isAlive. If it's not alive that would mean reading out past headlines
               is complete and it would proceed to read next headline.

         */

        processReadHeadlines = new Thread(new Runnable() {
            @Override
            public void run() {
                int size = headlines.size();
                currentHeadline = 0;
                while (currentHeadline < size && stopReadingFlag == 0) {
                    if (processTextToSpeechByArgv == null || !processTextToSpeechByArgv.isAlive()) {
                        textToSpeechByArgv(headlines.get(currentHeadline));
//                        while(processTextToSpeechByArgv.isAlive()){}
                        currentHeadline += 1;
                        System.out.println(i);
                    }
                }
                stopReadingFlag = 0;
            }
        });
        processReadHeadlines.start();

    }

    /**
     * Populates the ListView to the previous page of the headlines.
     *
     * @param e
     */
    public void populateListViewPrevPage(Event e) {
        listView.setItems(headlines);
        headlines.clear();
        headlinePageNumber--;

        new Thread(new Runnable() {
            @Override
            public void run() {
                scrapper.extractTopHeadlines(headlinePageNumber);
                for (String str : webScrapper.topHeadlines.keySet()) {
                    headlines.add(str);
                }
            }
        }).start();


    }

    /**
     * Stops the Text to Speech.
     *
     * @param e
     */
    public void stopTextToAudioByArgv(Event e) {

        if (processTextToSpeechByArgv != null && processTextToSpeechByArgv.isAlive()) {
            processTextToSpeechByArgv.destroy();
            textToAudioFlag = false; //This flag is set to false so that the currently running python file is exited.
        }


    }

    /**
     * Stops reading the news article.
     *
     * @param e
     */
    public void stopNewsToAudio(Event e) {

        if (processReadHeadlines != null && processReadHeadlines.isAlive()) {
            stopReadingFlag = 1;
        }
        stopTextToAudioByArgv(e);
        stopReadingFlag = 0;

    }

    /**
     * Whichever Row is in focused/(read by the UI) that row's article/content will be update in the
     * articleTextArea.
     */
    public void readArticleByEnter() {

        String selectedHeadline = headlines.get(currentHeadline - 1);
        reply = scrapper.extractArticleInformation(webScrapper.topHeadlines.get(selectedHeadline));
        articleTextArea.setText(reply);
    }

    /**
     * Reads the Shorcuts and usage, of current Scene infront of user.
     */
    public void help() {

        if (SCENE_ON_FRONT == 0) {
            textToSpeechByArgv("This mode will convert pdfs to braille , " +
                    "Shortcuts  , " + "Control + Q to select source file , " +
                    "Control + P to select Destination file ,  " +
                    "J to go to next page , " +
                    "F to go to previous page , " +
                    "G to go to desired page , " +
                    "U to upload the file to the eye device , " +
                    "R to Read the pdf preview or stopping the reader , " +
                    "Control + S for saving the file , "
            );

        } else if (SCENE_ON_FRONT == 1) {
            textToSpeechByArgv("This mode will convert text to braille and braille to text , ," +
                    "Shorcuts , " +
                    "T to get focus on text section , " +
                    "B to get focus on braille section , " +
                    "U to upload the content to Device , " +
                    "Control + P to select destination file , ");
        } else if (SCENE_ON_FRONT == 2) {
            textToSpeechByArgv("Customize UI ,");
        } else if (SCENE_ON_FRONT == 3) {
            textToSpeechByArgv("Enter commands to execute them, Either using braille keyboard ," +
                    "using keys S,D,F and J,K,L , " +
                    "Or using standard keyboard , " +
                    " Press K to toggle between Normal QWERT and Braille keyboard ,");
        } else if (SCENE_ON_FRONT == 4) {
            textToSpeechByArgv("This is the Learning mode, used for learning braille ," +
                    "Shortcuts , " +
                    " S to start iterating through every letter ," +
                    " T to randomly show a letter on Eye Device, allowing you guess the letter latter press enter to get the answer ," +
                    " G to go to specific letter which you type");
        } else if (SCENE_ON_FRONT == 5) {
            textToSpeechByArgv("This is the Dictionary Mode , " +
                    "Shortcuts , " +
                    " G to get focus on search field ," +
                    " D to Read out the Definition , " +
                    " A to Read out the Antonyms , " +
                    " S to Read out the Synonyms , ");
        } else if (SCENE_ON_FRONT == 7) {
            textToSpeechByArgv("This is the Wikipedia Mode ," +
                    "Shortcuts , " +
                    " G to get focus on search field , " +
                    " R to read out the wikipedia content , " +
                    " S to stop reading , ");
        } else if (SCENE_ON_FRONT == 6) {
            textToSpeechByArgv("This is the News Mode , " +
                    "Shortcuts , " +
                    "  H to read out the headlines , " +
                    " J to Fetches next headlines , " +
                    "  F to Fetches Previous headlines , " +
                    " Enter When used while reading headlines would get the full content about that headline , " +
                    "  R to read out the Article content , " +
                    "  S to stop reading ," +
                    " U to upload the content to the device.");
        }
    }


    public void searchDefinition(Event e) {
        isSpellcheckMode = false;

        String fieldText = searchWordField.getText();
        String word = fieldText.substring(0, 1).toUpperCase() + fieldText.substring(1).toLowerCase();
        String definition = dictionary.search(word);
        String synonym = "", antonym = "";

        String synAnt = dictionary.searchSynonyms(fieldText);

        if (synAnt.split("\n").length >= 2) {
            synonym = synAnt.split("\n")[0].split(":")[1];
            antonym = synAnt.split("\n")[1].split(":")[1];
        }


        definitionTextArea.setText(definition);
        synonymsTextArea.setText(synonym);
        antonymsTextArea.setText(antonym);

//        textToSpeechByArgv("Definition , " + definition + "Synonyms , " + synonym + "Antonyms , " + antonym);

        anchMain.requestFocus();

    }

    public void searchSpelling(Event e) {
        spellcheckArray.clear();
        isSpellcheckMode = true;
        String word = searchWordField.getText();
        String path = new File(".").getAbsolutePath();
        System.out.println("python " + path.replace(".", "spellcheck.py") + " " + "\"" + word + "\"");
        try {
            Process p = Runtime.getRuntime().exec("python " + path.replace(".", "spellcheck.py") + " " + "\"" + word + "\"");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String reply = bufferedReader.readLine();
            String[] answer = reply.split("\\|");
            for (int i = 0; i < answer.length; i++) {
                spellcheckArray.add(answer[i]);
            }
            bufferedReader.close();
        } catch (Exception k) {
            k.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();

        for (String i : spellcheckArray) {
            sb.append(i);
            sb.append("\n");
        }

        definitionTextArea.setText(sb.toString());
    }

    public void readSpellings(Event e) {


        Thread processReadSpelling = new Thread(new Runnable() {
            @Override
            public void run() {
                int size = spellcheckArray.size();
                curSpell = 0;
                System.out.println(size);
                while (curSpell < size && stopReadingFlag == 0) {

                    if (processTextToSpeechByArgv == null || !processTextToSpeechByArgv.isAlive()) {
                        textToSpeechByArgv(spellcheckArray.get(curSpell));
                        curSpell += 1;
                    }
                }
                stopReadingFlag = 0;
            }
        });
        processReadSpelling.start();


    }


    public void readEachChar() {

        stopReadingFlag = 0;
        Thread processReadSpelling = new Thread(new Runnable() {
            @Override
            public void run() {
                int size = spellcheckArray.get(curSpell - 1).length();
                int len = 0;
                while (len < size && stopReadingFlag == 0) {
                    if (processTextToSpeechByArgv == null || !processTextToSpeechByArgv.isAlive()) {
                        textToSpeechByArgv(spellcheckArray.get(curSpell - 1).substring(len, len + 1));
                        len += 1;
                        System.out.println(len);
                    }
                }
                stopReadingFlag = 0;
            }
        });
        processReadSpelling.start();
    }


    public void definationToAudio(Event e) {
        String defination = definitionTextArea.getText();
        stopTextToAudioByArgv(e);
        textToSpeechByArgv("Defination, " + defination);
    }

    public void synonymsToAudio(Event e) {
        String synonyms = synonymsTextArea.getText();
        stopTextToAudioByArgv(e);
        textToSpeechByArgv("Synonyms , " + synonyms);
    }

    public void antonymsToAudio(Event e) {
        String synonyms = antonymsTextArea.getText();
        stopTextToAudioByArgv(e);
        textToSpeechByArgv("Antonyms , " + synonyms);
    }


    public void textToSpeechByArgv(String s) {
        int initialPosition = 0;
        int endPosition = 8000;
        int finalPosition = s.length();
        if (processTextToSpeechByArgv != null) {
            processTextToSpeechByArgv.destroy();
        }
        try {
            String path = new File(".").getAbsolutePath();
            System.out.println("Inside textToSpeech");
            do {
                if (processTextToSpeechByArgv == null || (processTextToSpeechByArgv != null) && !processTextToSpeechByArgv.isAlive()) {
                    if ((finalPosition - initialPosition) > 8000) {
                        String command = (new File(".").getAbsolutePath()).replace(".", "pyFiles/textToAudioByArgv " + "\"" + s.substring(initialPosition, endPosition) + "\""); //"python " + path.replace(".", "textToAudioByArgv.py  " + "\"" + s.substring(initialPosition, endPosition) + "\"")
                        System.out.println(s.substring(initialPosition, endPosition));
                        processTextToSpeechByArgv = Runtime.getRuntime().exec(command);
                        initialPosition = endPosition - 1;
                        endPosition = (finalPosition - initialPosition) > 8000 ? endPosition + 8000 : (finalPosition - initialPosition);
                        System.out.println("end");
                    } else {
                        System.out.println(new File(".").getAbsolutePath());
                        String command = (new File(".").getAbsolutePath()).replace(".", "pyFiles/textToAudioByArgv " + "\"" + s + "\"");
//                        String command = (new File(".").getAbsolutePath()).replace(".","src/sample//PythonPrograms/textToAudioByArgv " + "\"" + s + "\"");
//                        System.out.println(getClass().getResource("sample/PythonPrograms/textToAudioByArgv.exe").getPath());
                        processTextToSpeechByArgv = Runtime.getRuntime().exec(command);
                        initialPosition = finalPosition;
                    }
                }
            } while ((initialPosition < finalPosition && textToAudioFlag));
        } catch (Exception r) {
            r.printStackTrace();
        }
        textToAudioFlag = true;
    }

    public void searchWikipedia(Event e) {
        String query = searchWikipediaField.getText();
        if (query.length() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    reply = scrapper.getContentFromWikipedia(query);
                    wikipediaTextArea.setText(reply);
                    if (reply.length() > 0) {
                        System.out.println("Voice wiki");
                        textToSpeechByArgv(reply);
                    } else {
                        String confirmation = "System was unable to find information regarding " + query;
                        wikipediaTextArea.setText(confirmation);
                        textToSpeechByArgv(confirmation);
                    }
                }
            }).start();


        }
        anchMain.requestFocus();
    }

    public void readWikipedia(Event e) {
        textToSpeechByArgv(wikipediaTextArea.getText());
    }


    public void srcFileChooser(Event event) {
        //used to set source of pdf
//        speechToTextLib.speak("selectSrcPDF");
        textToSpeechByArgv("Please Select the source file");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(DEFAULT_SRC_DIR + ":\\"));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PDF Files", "*.pdf");
        fc.getExtensionFilters().add(extensionFilter);
        File choosenFile = fc.showOpenDialog(stage);
        try {
            src = choosenFile.getAbsolutePath();

            sourceText.setText(src);
        } catch (Exception e) {
            new Thread(new Task<Void>() {
                @Override
                public Void call() {
//                    SpeechToTextLib.speak("srcFileNotSelected");
                    textToSpeechByArgv("Source file not selected");
                    return null;
                }
            }).start();
        }
    }

    @FXML
    public void outputFileChooser(Event event) {
//        speechToTextLib.speak("selectDstPDF");
        textToSpeechByArgv("Please Select the output file");
        FileChooser fc = new FileChooser();

        try {
            File choosenFile = fc.showOpenDialog(stage);
            dst = choosenFile.getAbsolutePath();
            outputTextField.setText(dst);
        } catch (Exception e) {
            new Thread(new Task<Void>() {
                @Override
                public Void call() {
//                    SpeechToTextLib.speak("dstFileNotSelected");
                    textToSpeechByArgv("Output File not selected");
                    return null;
                }
            }).start();
        }

    }


    public void loadNextPage(Event event) {
        braillePdfTextArea.clear();
        if (p != null && p.isAlive()) {
            p.destroy();
        }
        try {
            PdfReader pfile = new PdfReader(src);
            String output = "";
            String str = PdfTextExtractor.getTextFromPage(pfile, ++pageNo);
            for (char c : str.toCharArray()) {
                output += c;
            }
            braillePdfTextArea.setText(output);
            if (readOnChangeChbx.isSelected()) {
                readOutLoud(event);
            }
        } catch (Exception e) {
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    SpeechToTextLib.speak("pdfFileNotSelected");
                    return null;
                }
            }).start();
            brailleTextArea.setText("Please Select the PDF to read..");
        }

    }

    public void loadPrevPage(Event event) {
        braillePdfTextArea.clear();
        //To Stop the Narration if its running
        if (p != null && p.isAlive()) {
            p.destroy();
        }
        try {
            PdfReader pfile = new PdfReader(src);
            String output = "";

            String str = PdfTextExtractor.getTextFromPage(pfile, --pageNo);
            for (char c : str.toCharArray()) {
                output += c;
            }

            braillePdfTextArea.setText(output);
        } catch (Exception e) {
            new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    SpeechToTextLib.speak("pdfFileNotSelected");
                    return null;
                }
            }).start();
            brailleTextArea.setText("Please Select the PDF to read..");
        }


    }

    public void goToPage(Event e) {
        GOTO_PAGE = Integer.parseInt(goToField.getText());
        pageNo = GOTO_PAGE - 1;
        loadNextPage(e);
    }

    public void goToPageByEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            goToPage(e);
            goToField.clear();
            anchMain.requestFocus();
        }
    }


    public void savePdfToBraille(Event e) throws InterruptedException, IOException {
        // FIXME: 01-12-2018 
        Thread thread = new Thread(new Task<Void>() {
            @Override
            protected Void call() throws IOException {

                FileWriter file = null;
                PdfReader pfile = null;
                try {
                    pfile = new PdfReader(src);
                } catch (Exception e1) {

                    i = 0;

                }
                if (pfile != null) {
                    file = null;
                    try {
                        file = new FileWriter(dst);
                    } catch (Exception e1) {
                        i = 1;
                    }
                    if (file != null) {
                        brailleConverter convert = new brailleConverter();
//                int n=1;
                        try {
                            if (rangeCheckBox.isSelected()) {
                                for (int i = Integer.parseInt(rangeFrom.getText()); i <= Integer.parseInt(rangeTo.getText()); i++) {
                                    for (char c : PdfTextExtractor.getTextFromPage(pfile, i).toCharArray()) {
                                        file.write(convert.convertChar(c));
                                    }
                                }
                            }
                            file.write("Text Added");

                            for (char c : PdfTextExtractor.getTextFromPage(pfile, pageNo).toCharArray()) {
                                file.write(convert.convertChar(c));
                                System.out.println(convert.convertChar(c) + " " + c);
                            }
                            i = 2;
                        } catch (Exception e1) {
                            i = 3;
                        } finally {
                            file.close();
                        }
                    }
                }
                return null;
            }
        });

        thread.start();

        thread.join();

        if (i == 0) {
            SpeechToTextLib.speak("srcFileNotSelected");

            savePdfConfirmLabel.setStyle("-fx-text-fill:red;");
            savePdfConfirmLabel.setText("Source File Not Selected..");

        } else if (i == 1) {

            SpeechToTextLib.speak("dstFileNotSelected");
            savePdfConfirmLabel.setText("Destination File Not Selected..");
        } else if (i == 2) {

            SpeechToTextLib.speak("conversionSuccessful");

            savePdfConfirmLabel.setStyle("-fx-text-fill:green;");
            savePdfConfirmLabel.setText("Successful Conversion");
        } else {

            SpeechToTextLib.speak("conversionNotSuccessful");
            savePdfConfirmLabel.setText("Failed to Convert");
            savePdfConfirmLabel.setStyle("-fx-text-fill : red;");
        }
    }

    @FXML
    public void displayBraille(Event event) throws IOException {
        String txt = writtenTextArea.getText();
        String outputBraille = "";
        brailleConverter convert = new brailleConverter();
        for (char str : txt.toCharArray()) {
            outputBraille += convert.convertChar(str);
        }
        brailleTextArea.setText(outputBraille);

    }

    @FXML
    public void saveBraille(Event e) {
        try {
            FileWriter file = new FileWriter(dst);
            file.write(brailleTextArea.getText());
            System.out.println(dst + brailleTextArea.getText());
            file.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @FXML
    private void appClose(ActionEvent event) {

        if (isSerialAlive) {
            try {
                outputStreamForSerial.close();
                inputStreamForSerial.close();
            } catch (IOException e) {
                e.printStackTrace();
                Platform.exit();
                System.exit(0);
            }
            comPort.closePort();
        }
        stopTextToAudioByArgv(event);
        stopNewsToAudio(event);
        Platform.exit();
        System.exit(0);
    }

    public void enlargeBtn(Event event) {
        Button tmpBtn;
        tmpBtn = (Button) event.getSource();
        double width = tmpBtn.getWidth();
        double height = tmpBtn.getHeight();
        tmpBtn.setMinSize(width + 6, height + 6);
    }

    public void minimizeBtn(Event event) {
        Button tmpBtn = (Button) event.getSource();
        double width = tmpBtn.getWidth();
        double height = tmpBtn.getHeight();
        tmpBtn.setMinSize(width - 6, height - 6);

    }


    public void changeSceneToPdf(Event event) {
        outputTextField.setText("");
        brailleTextArea.clear();
        writtenTextArea.clear();
        anchPdf.toFront();
        SCENE_ON_FRONT = 0;

        textToSpeechByArgv("PDF To Braille");
    }

    public void changeSceneToText(Event event) {

        sourceText.setText("");
        anchText.toFront();
        SCENE_ON_FRONT = 1;

        textToSpeechByArgv("Text to Braille");
    }

    public void changeSceneToCustomize(Event e) {
        anchCustomize.toFront();
        SCENE_ON_FRONT = 2;

    }

    public void changeSceneToLearningMode(Event e) {
        anchLearningMode.toFront();
        SCENE_ON_FRONT = 3;

        textToSpeechByArgv("Learning Mode");
    }

    public void changeSceneToWikipedia(Event e) {
        anchWikipedia.toFront();
        SCENE_ON_FRONT = 7;

        textToSpeechByArgv("Wikipedia Mode");
    }

    public void changeSceneToDictionary(Event e) {
        anchDictonary.toFront();
        SCENE_ON_FRONT = 5;

        textToSpeechByArgv(" Dictionary Mode");
    }


    public void changeSceneToNews(Event e) {
        anchNews.toFront();
        SCENE_ON_FRONT = 6;

        textToSpeechByArgv("News Mode");


        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println(new_val);
                                String articleLink = webScrapper.topHeadlines.get(new_val);
                                System.out.println(articleLink);
                                String article = scrapper.extractArticleInformation(articleLink);
                                articleTextArea.setText(article);

                            }
                        }).start();

                    }
                });
    }


    public void changeSceneToOCR(Event e) {
        SCENE_ON_FRONT = 8;
        anchOcr.toFront();
        textToSpeechByArgv("OCR Mode");
    }

    public void changeSceenToQuizMode(Event e) {
        SCENE_ON_FRONT = 9;
        anchQuiz.toFront();

        textToSpeechByArgv("Quiz Mode");
    }


    public void applyColorScheme(Event event) {

        Color pColor = previewColor.getValue();
        Color bColor = buttonColor.getValue();
        Color bgColor = backgroundColor.getValue();
        Color tColor = textColor.getValue();
        Color bdColor = borderColor.getValue();

        String btnColor = "-fx-background-color : " + bColor.toString().replace("0x", "#") + ";";
        String backgroundColor = "-fx-background-color : " + bgColor.toString().replace("0x", "#") + ";";
        String txtColor = "-fx-text-fill :" + tColor.toString().replace("0x", "#") + ";";
        String prevColor = "-fx-control-inner-background : " + pColor.toString().replace("0x", "#") + ";";
        String brColor = "-fx-border-color : " + bdColor.toString().replace("0x", "#") + ";";

        String radiusString = "-fx-background-radius : 100 ; -fx-border-radius : 100 ; -fx-border-width : 3;";

        //Setting values of Buttons;
        System.out.println("changing button color");
        minimizeBtn.setStyle(btnColor + txtColor);
        closeBtn.setStyle(btnColor + txtColor);
        applyCustomizeColor.setStyle(btnColor + txtColor);
        btnNextpage.setStyle(btnColor + txtColor);
        btnPrevpage.setStyle(btnColor + txtColor);
        btnPrint.setStyle(btnColor + txtColor);
        btnOutputfile.setStyle(btnColor + txtColor);
        btnSave.setStyle(btnColor + txtColor);
        btnPdfDestination.setStyle(btnColor + txtColor);
        btnPdfSource.setStyle(btnColor + txtColor);
        btnGoTo.setStyle(btnColor + txtColor);

        btnLearningTest.setStyle(btnColor + txtColor);
        btnLearningStart.setStyle(btnColor + txtColor);
        btnLearnGo.setStyle(btnColor + txtColor);
        btnLearningRestart.setStyle(btnColor + txtColor);

        btnUploadTxtToBraille.setStyle(btnColor + txtColor);

        btnReadDefinition.setStyle(btnColor + txtColor);
        btnUploadDefinitionToBraille.setStyle(btnColor + txtColor);
        btnStopReadingDictionary.setStyle(btnColor + txtColor);
        btnSearchWord.setStyle(btnColor + txtColor);
        btnSpellcheck.setStyle(btnColor + txtColor);

        btnNextNews.setStyle(btnColor + txtColor);
        btnPrevNews.setStyle(btnColor + txtColor);
        btnReadHeadlines.setStyle(btnColor + txtColor);
        btnReadArticle.setStyle(btnColor + txtColor);
        btnStopReadingNews.setStyle(btnColor + txtColor);

        btnReadWikipedia.setStyle(btnColor + txtColor);
        btnSearchWikipedia.setStyle(btnColor + txtColor);
        btnUploadWikipedia.setStyle(btnColor + txtColor);
        btnStopReadingWikipedia.setStyle(btnColor + txtColor);


        btnSourceImage.setStyle(btnColor + txtColor);
        btnOcrRead.setStyle(btnColor + txtColor);
        btnOcrUpload.setStyle(btnColor + txtColor);

        btnNextQuiz.setStyle(btnColor + txtColor);
        btnPrevQuiz.setStyle(btnColor + txtColor);
        btnSubmitQuiz.setStyle(btnColor + txtColor);
        btnResetQuiz.setStyle(btnColor + txtColor);

        btnSetSerial.setStyle(btnColor + txtColor);

        //background Color
        System.out.println("Changing background Color");
        anchPdf.setStyle(backgroundColor);
        anchText.setStyle(backgroundColor);
        anchCustomize.setStyle(backgroundColor);
        anchLearningMode.setStyle(backgroundColor);
        anchDictonary.setStyle(backgroundColor);
        anchNews.setStyle(backgroundColor);
        anchWikipedia.setStyle(backgroundColor);
        anchQuiz.setStyle(backgroundColor);
        anchOcr.setStyle(backgroundColor);

        //text Color
        System.out.println("changing text color" + txtColor);
        sourceText.setFill(tColor);
        textPreview.setFill(tColor);
        outputTextField.setFill(tColor);
        tText.setStyle(txtColor);
        tPreview.setStyle(txtColor);
        tBackground.setStyle(txtColor);
        tCustomize.setStyle(txtColor);
        tColorScheme.setStyle(txtColor);
        tButton.setStyle(txtColor);
        tBorder.setStyle(txtColor);
        tEquals.setFill(tColor);
        tNewsTitle.setStyle(txtColor);

        tLearn.setStyle(txtColor);
        tLearnInfo1.setStyle(txtColor);
        tLearnInfo2.setStyle(txtColor);
        tTest.setStyle(txtColor);
        tTestInfo.setStyle(txtColor);
        tDelay.setStyle(txtColor);
        String strokeForLine = "-fx-stroke:" + bdColor.toString().replace("0x", "#") + ";";
        lVert1.setStyle(strokeForLine);
        lVert2.setStyle(strokeForLine);
        lVert3.setStyle(strokeForLine);
        lHor1.setStyle(strokeForLine);

        tWikipediaTitle.setStyle(txtColor);
        tSearchWikipedia.setStyle(txtColor);
        wLine1.setStyle(strokeForLine);
        wLine2.setStyle(strokeForLine);
        wLine3.setStyle(strokeForLine);

        tImageSource.setStyle(txtColor);
        imageSourceField.setStyle(txtColor);


        tQuizMode.setStyle(txtColor);
        tProblemStatement.setStyle(txtColor);
        tAnswer.setStyle(txtColor);
        tCorrection.setStyle(txtColor);
        tQuetionNumber.setStyle(txtColor);


        tSearch.setStyle(txtColor);
        tDefination.setStyle(txtColor);
        tSynonyms.setStyle(txtColor);
        tAntonyms.setStyle(txtColor);
        tDictionaryTitle.setStyle(txtColor);
        line1.setStyle(strokeForLine);
        line2.setStyle(strokeForLine);
        line3.setStyle(strokeForLine);

        qLine1.setStyle(strokeForLine);
        qLine2.setStyle(strokeForLine);
        qLine3.setStyle(strokeForLine);
        qLine4.setStyle(strokeForLine);

        vertLineTextToBraille.setStyle(strokeForLine);

        textLabelTextToBraille.setStyle(txtColor);
        BrailleLabelTextToBraille.setStyle(txtColor);


        rangeFrom.setStyle(prevColor);
        rangeCheckBox.setStyle(txtColor + "-fx-color:" + bColor.toString().replace("0x", "#") + ";");
        rangeTo.setStyle(prevColor);
        goToField.setStyle(prevColor);
        keyboardChbx.setStyle("-fx-color:" + bColor.toString().replace("0x", "#") + ";");

        tSerialMode.setStyle(txtColor);
        serialPortChoice.setStyle(txtColor);
        //preview Color

        System.out.println("changing preview color");
        braillePdfTextArea.setStyle(prevColor + txtColor);
        brailleTextArea.setStyle(prevColor + txtColor);
        writtenTextArea.setStyle(prevColor + txtColor);
        commandPromtPreview.setStyle(prevColor + txtColor + brColor + "-fx-border-width : 8;");
        timeIntervalField.setStyle(prevColor + txtColor + brColor);
        learnGoToField.setStyle(prevColor + txtColor + brColor);
        txtToBrailleDelayField.setStyle(prevColor + txtColor + brColor);

        searchWordField.setStyle(prevColor + txtColor + brColor);
        searchWikipediaField.setStyle(prevColor + txtColor + brColor);
        definitionTextArea.setStyle(prevColor + txtColor + brColor);
        synonymsTextArea.setStyle(prevColor + txtColor + brColor);
        antonymsTextArea.setStyle(prevColor + txtColor + brColor);
        articleTextArea.setStyle(prevColor + txtColor + brColor);

        ocrTextArea.setStyle(prevColor + txtColor + brColor);
        imageSourceField.setStyle(prevColor + txtColor + brColor);

        quizTextArea.setStyle(prevColor + txtColor + brColor);
        quizAnswerField.setStyle(prevColor + txtColor + brColor);

        wikipediaTextArea.setStyle(prevColor + txtColor + brColor);
        //circles Color
        btnCustom.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnPdfToBraille.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnTextToBraille.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnLearningMode.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnDictionaryToBraille.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnWikipediaToBraille.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnNewsToBraille.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnOcrMode.setStyle(brColor + radiusString + backgroundColor + txtColor);
        btnQuizMode.setStyle(brColor + radiusString + backgroundColor + txtColor);

        listView.setStyle(prevColor);

        listView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(final ListView<String> list) {
                return new ListCell<String>() {
                    {
                        Text text = new Text();
                        text.wrappingWidthProperty().bind(list.widthProperty().subtract(30));
                        text.textProperty().bind(itemProperty());
                        text.setStyle("-fx-font-size:18;");
                        text.setFill(Color.web(tColor.toString().replace("0x", "#")));
                        setPrefWidth(0);

                        setGraphic(text);


                    }
                };
            }
        });

    }

    String q;
    Thread thread;
    Thread thread2;

    public void activateVoiceTranslation(KeyEvent e) {


        if (e.getCode() == KeyCode.SHIFT) {
            startRecording();

        } else if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.J) {
            if (SCENE_ON_FRONT == 0) {
                loadNextPage(e);
            } else if (SCENE_ON_FRONT == 6) {
                btnNextNews.fire();
            } else if (SCENE_ON_FRONT == 9) {
                nextQuiz(e);
            }
        } else if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.F) {
            if (SCENE_ON_FRONT == 0) {
                loadPrevPage(e);
            } else if (SCENE_ON_FRONT == 6) {
                btnPrevNews.fire();
            } else if (SCENE_ON_FRONT == 9) {
                prevQuiz(e);
            }

        } else if (e.getCode() == KeyCode.R) {
            if (SCENE_ON_FRONT == 0) {
                if (!isReaderOn) {
                    readOutLoud(e);
                    isReaderOn = true;
                } else {
                    System.out.println("closed");
                    stopTextToAudioByArgv(e);
                    isReaderOn = false;
                }
            } else if (SCENE_ON_FRONT == 6) {
                stopNewsToAudio(e);
                readArticle(e);

            } else if (SCENE_ON_FRONT == 5 && !searchWordField.isFocused()) {
                stopTextToAudioByArgv(e);
            } else if (SCENE_ON_FRONT == 7 && !searchWikipediaField.isFocused()) {
                readWikipedia(e);
            } else if (SCENE_ON_FRONT == 8) {
                readTextFromImage();
            } else if (SCENE_ON_FRONT == 9) {
                readQuestion(e);
            }
        } else if (e.isControlDown() && e.getCode() == KeyCode.S && SCENE_ON_FRONT == 0) {
            try {
                savePdfToBraille(e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } else if (e.isControlDown() && e.getCode() == KeyCode.S && SCENE_ON_FRONT == 1) {
            saveBraille(e);
        } else if (e.getCode() == KeyCode.T) {
            if (SCENE_ON_FRONT == 3) {
                testMode(e);
            }

        } else if (e.getCode() == KeyCode.L && SCENE_ON_FRONT == 3) {
            startLearnMode(e);
        } else if (e.getCode() == KeyCode.K) {
            if (keyboardChbx.isSelected()) {
                keyboardChbx.setSelected(false);
            } else if (!keyboardChbx.isSelected()) {
                keyboardChbx.setSelected(true);
            }
        } else if (e.getCode() == KeyCode.I) {
            restartStepper(e);  //will get the stepper motor to its initial position.
        }

    }

    private void startRecording() {
        circleConfirmSpeech.setCenterY(circleConfirmSpeech.getCenterY() - 70);
        circleConfirmSpeech.setStyle("-fx-fill: #a2ff70;");
        speechToTextLib.shiftPressed = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String query = speechToTextLib.ConvertToText();
                if (query == "1") {
                    flag = 1;
                }
            }
        });
        thread.start();
    }


    public void deactivateVoiceTranslation(KeyEvent e) throws InterruptedException {
        if (e.getCode() == KeyCode.SHIFT) {
            circleConfirmSpeech.setCenterY(circleConfirmSpeech.getCenterY() + 70);
            speechToTextLib.shiftPressed = false;
            System.out.println("inside deactivate");
//            thread.join();

            thread2 = new Thread(new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    q = speechToTextLib.sendRequest();
                    System.out.println(q);
                    circleConfirmSpeech.setStyle("-fx-fill: #7687bc;");
                    System.out.println(writtenTextArea.isFocused());
                    if (writtenTextArea.isFocused()) {
                        writtenTextArea.setText(q);
                    } else {
                        actionOnCommand(q, e);
                        System.out.println("translation:- " + q);
                    }
                    actionOnCommand(q, e);
                    return null;
                }
            });
            thread2.start();
        } else if (e.isControlDown() && e.getCode() == KeyCode.H) {
            help();
        } else if (e.isControlDown() && e.getCode() == KeyCode.P && (SCENE_ON_FRONT == 1 || SCENE_ON_FRONT == 0)) {
            btnPdfDestination.fire();
        } else if (e.isControlDown() && e.getCode() == KeyCode.Q && (SCENE_ON_FRONT == 1 || SCENE_ON_FRONT == 0)) {
            btnPdfSource.fire();
        } else if (e.getCode() == KeyCode.P && e.isAltDown()) {
            changeSceneToPdf(e);
        } else if (e.isAltDown() && e.getCode() == KeyCode.L) {
            changeSceneToLearningMode(e);
            anchLearningMode.requestFocus();
        } else if (e.isAltDown() && e.getCode() == KeyCode.N) {
            changeSceneToNews(e);
        } else if (e.isAltDown() && e.getCode() == KeyCode.D) {
            changeSceneToDictionary(e);
        } else if (e.isAltDown() && e.getCode() == KeyCode.W) {
            changeSceneToWikipedia(e);
        } else if (e.isAltDown() && e.getCode() == KeyCode.T) {
            changeSceneToText(e);
        } else if (e.getCode() == KeyCode.C && e.isAltDown()) {
            commandPromtPreview.requestFocus();
        } else if (e.isControlDown() && e.getCode() == KeyCode.L) {
            changeThemeLight(e);
        } else if (e.isAltDown() && e.getCode() == KeyCode.Q) {
            changeSceenToQuizMode(e);
        } else if (e.isAltDown() && e.getCode() == KeyCode.O) {
            changeSceneToOCR(e);
        } else if (e.isControlDown() && e.getCode() == KeyCode.D) {
            changeThemeDarcula(e);
        } else if (e.isControlDown() && e.getCode() == KeyCode.M) {
            anchMain.requestFocus();
        } else if (e.getCode() == KeyCode.D) {
            if (SCENE_ON_FRONT == 5 && !searchWordField.isFocused()) {
                definationToAudio(e);
            }
        } else if (e.getCode() == KeyCode.A) {
            if (SCENE_ON_FRONT == 5 && !searchWordField.isFocused()) {
                antonymsToAudio(e);
            } else if (SCENE_ON_FRONT == 9 && !quizAnswerField.isFocused()) {
                quizAnswerField.clear();
                quizAnswerField.requestFocus();

            }

        } else if (e.getCode() == KeyCode.ENTER) {
            if (SCENE_ON_FRONT == 6) {
                System.out.println(currentHeadline);
                stopNewsToAudio(e);
                readArticleByEnter();
            } else if (SCENE_ON_FRONT == 5 && isSpellcheckMode) {
                stopReadingFlag = 1;
                readEachChar();
            }
        } else if (e.getCode() == KeyCode.S) {
            System.out.println("s is pressed");
            if (SCENE_ON_FRONT == 6) {
                stopNewsToAudio(e);
            } else if (SCENE_ON_FRONT == 5 && !searchWordField.isFocused() && !isSpellcheckMode) {
                synonymsToAudio(e);
            } else if (SCENE_ON_FRONT == 5 && isSpellcheckMode) {
                System.out.println("reading spellings");
                readSpellings(e);
            } else if (true/*SCENE_ON_FRONT == 7 && !searchWikipediaField.isFocused()*/) {
                stopTextToAudioByArgv(e);
            }
        } else if (e.getCode() == KeyCode.H) {
            if (SCENE_ON_FRONT == 6) {
                readHeadlines(e);
            }
        } else if (e.getCode() == KeyCode.U) {
            if (SCENE_ON_FRONT == 0) {
                System.out.println("focus set");
                pdfToBrailleDelayField.requestFocus();
                pdfToBrailleDelayField.clear();
            } else if (SCENE_ON_FRONT == 1 && !writtenTextArea.isFocused()) {
                txtToBrailleDelayField.requestFocus();
                txtToBrailleDelayField.clear();
            } else if (SCENE_ON_FRONT == 4 && !searchWordField.isFocused()) {
                System.out.println("yet to implement upload in dictionary");
            }
        } else if (e.getCode() == KeyCode.G) {
            if (SCENE_ON_FRONT == 0) {
                goToField.requestFocus();
            } else if (SCENE_ON_FRONT == 3) {
                learnGoToField.requestFocus();
            } else if (SCENE_ON_FRONT == 5) {
                searchWordField.requestFocus();
            } else if (SCENE_ON_FRONT == 7) {
                searchWikipediaField.requestFocus();
            }
        } else if (e.getCode() == KeyCode.B) {
            if (SCENE_ON_FRONT == 1) {
                brailleTextArea.requestFocus();
            }
        } else if (e.getCode() == KeyCode.T) {
            if (SCENE_ON_FRONT == 1) {
                writtenTextArea.requestFocus();
            }
        } else if (e.getCode() == KeyCode.C) {
            if (SCENE_ON_FRONT == 5) {
                btnSpellcheck.fire();
            }
        }


    }

    public void actionOnCommand(String query, KeyEvent e) {
        if (query.contains("close")) {     //Closes the Entire Application
            appClose(new ActionEvent());
        } else if ((query.contains("open") || query.contains("source") || query.contains("choose")) && (query.contains("file") || query.contains("pdf")) && isFocused(0)) {
            speechToTextLib.speak("selectSrcPDF");
            btnPdfSource.fire();
        } else if (query.contains("choose") || query.contains("open") && (query.contains("destination") || query.contains("output") || query.contains("file"))) {
            speechToTextLib.speak("selectDstPDF");
            if (isFocused(0)) {
                btnPdfDestination.fire();
            } else if (isFocused(1)) {
                btnOutputfile.fire();
            }
        } else if (query.contains("turn") || query.contains("go") || query.contains("to")) {
            String a, b;
            StringTokenizer st = new StringTokenizer(query);

            if (st.countTokens() == 3) {
                a = st.nextToken();
                b = st.nextToken();
                if (a.equals("go") && b.equals("to")) {
                    int pgn = Integer.parseInt(st.nextToken());
                    pageNo = pgn - 1;
                    btnNextpage.fire();
                }
            }
            if (st.countTokens() == 2) {
                a = st.nextToken();
                b = st.nextToken();
                if (a.equals("go") && b.equals("to")) {
                    SpeechToTextLib.speak("enterPageNo");
                    goToField.requestFocus();
                }
            }
        } else if (query.contains("next") && isFocused(0)) {
            btnNextpage.fire();
        } else if (query.contains("previous") && isFocused(0)) {

            btnPrevpage.fire();
        } else if (query.contains("save") && (query.contains("pdf") || query.contains("file"))) {
            if (isFocused(1)) {
                btnSave.fire();
            } else if (isFocused(0)) {
                btnPrint.fire();
            }
        } else if (query.contains("use") && query.contains("range")) {
            //incomplete code- further report user to enter from and to number
            System.out.println("Using page number by range");
        } else if (query.contains("convert") && query.contains("pdf")) {
            btnPdfToBraille.fire();
        } else if (query.contains("convert") && query.contains("text")) {
            btnTextToBraille.fire();
        } else if (query.contains("read")) {
            readOutLoud(e);
        } else if (query.contains("start") && query.contains("learning") && query.contains("mode")) {
            changeSceneToLearningMode(e);
        } else if (query.contains("search") && query.contains("start") && query.contains("news")) {
            changeSceneToNews(e);
        } else if (query.contains("read") && query.contains("headlines")) {
            changeSceneToNews(e);
            if (headlines.isEmpty()) {
                populateListViewNextPage(e);
            }
            readHeadlines(e);
        } else if (query.contains("search") && query.contains("dictionary")) {
            changeSceneToDictionary(e);
        } else if (query.contains("search") && query.contains("wikipedia")) {
            changeSceneToNews(e);
        } else if (query.contains("define")) {
            String[] lst = query.split(" ");
            searchWordField.setText(lst[1]);
            btnSearchWord.fire();
        }
    }


    String brailleKeysPressed = "";

    public void brailleKeyboardPressed(KeyEvent e) {
//        System.out.println(e.getCode().toString());
        if (keyboardChbx.isSelected()) {
            if (e.getCode() == KeyCode.F) {
                brailleKeysPressed = brailleKeysPressed + "1";
            } else if (e.getCode() == KeyCode.D) {
                brailleKeysPressed = brailleKeysPressed + "2";
            } else if (e.getCode() == KeyCode.S) {
                brailleKeysPressed = brailleKeysPressed + "3";
            } else if (e.getCode() == KeyCode.J) {
                brailleKeysPressed = brailleKeysPressed + "4";
            } else if (e.getCode() == KeyCode.K) {
                brailleKeysPressed = brailleKeysPressed + "5";
            } else if (e.getCode() == KeyCode.L) {
                brailleKeysPressed = brailleKeysPressed + "6";
            } else if (e.getCode() == KeyCode.SPACE) {
                brailleKeysPressed = "9";
            }
        }

        if (e.getCode() == KeyCode.ENTER) {
            actionOnCommand(commandPromtPreview.getText(), e);
            commandPromtPreview.setText("");
            anchMain.requestFocus();
        }
    }

    public void brailleKeyboardReleased(KeyEvent e) {
        if (keyboardChbx.isSelected()) {

            System.out.println(brailleKeysPressed);
            String prevStr = commandPromtPreview.getText();
            String out = converter.brailleKeyboardToChar(brailleKeysPressed);
            if (!out.equals("n")) {
                commandPromtPreview.setText(prevStr + out);
            } else if (e.getCode() == KeyCode.BACK_SPACE) {
//                String prevStr = commandPromtPreview.getText();
                System.out.println(prevStr.length());
                commandPromtPreview.setText(prevStr.substring(0, prevStr.length() - 1));
            }
            brailleKeysPressed = "";
        }
    }

    public void keyboardChooser(Event e) {
        if (keyboardChbx.isSelected()) {
            commandPromtPreview.setEditable(false);
        } else if (!keyboardChbx.isSelected()) {
            commandPromtPreview.setText("");
            commandPromtPreview.setEditable(true);
        }
    }


    //Reading out the current Text inside the preview,p to end
    public void readOutLoud(Event e) {
//        writePdfToText(); //to write the preview to text file named pdf.txt
        String str = braillePdfTextArea.getText();
        textToSpeechByArgv(str);
    }

    //--For Changing theme--//
    public void changeThemeDarcula(Event e) {
        currentTheme = "dark";
        backgroundColor.setValue(Color.web("#282a36"));
        borderColor.setValue(Color.web("#b3b3b3"));
        previewColor.setValue(Color.web("#44475a"));
        buttonColor.setValue(Color.web("#4077b3"));
        textColor.setValue(Color.web("#cccccc"));
        applyCustomizeColor.fire();
    }

    public void changeThemeLight(Event e) {
        currentTheme = "light";
        backgroundColor.setValue(Color.web("#F2EECB"));
        borderColor.setValue(Color.web("#F2BC12"));
        previewColor.setValue(Color.web("#faf7e1"));
        buttonColor.setValue(Color.web("#decf5f"));
        textColor.setValue(Color.web("#908706"));
        applyCustomizeColor.fire();
    }

    private void startArduinoStepper() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = new File(".").getAbsolutePath();
                String Command = "python " + path.replace(".", "startStepper.py");
                try {
                    processForArduinoStepper = Runtime.getRuntime().exec(Command);
                    isStepperAlive = true;
                } catch (Exception e) {
                    System.out.println("error running stepper");
                }
            }
        }).start();
    }

    public void startSerialPort() {
//        System.out.println("In startSerialPort " + comPort.isOpen());
//        if(comPort==null || !comPort.isOpen()) {
        SerialPort[] sp = SerialPort.getCommPorts();
        comPort = SerialPort.getCommPorts()[0];
        System.out.println(comPort);
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);
        inputStreamForSerial = comPort.getInputStream();
        outputStreamForSerial = comPort.getOutputStream();
        isSerialAlive = true;
        try {
            Thread.sleep(2200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        }
    }

    public void rotateToChar() {
        threadRotateToChar = new Thread(new Runnable() {
            @Override
            public void run() {
                String d = timeIntervalField.getText();
                Float delay;
                if (d.equals("")) {
                    delay = Float.valueOf(2);
                } else {
                    delay = Float.parseFloat(d);
                }

                if (!isSerialAlive) {
                    startSerialPort();
                } else if (!isStepperAlive) {
                    startArduinoStepper();
                }

                System.out.println("inside learning mode");

                rotateStringOnStepper(str, delay);
            }
        });
        threadRotateToChar.start();
    }

    public void rotateStringOnStepper(String stringToRepresent, float delay) {
        currentStepsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= (stringToRepresent.length() - 1); i += 3) {
                    try {
                        System.out.println(i);
                        if (restartRotation) {
                            outputStreamForSerial.write("aaa".getBytes());
//                            restartStepper(null);
                            restartRotation = false;
                            break;
                        } else {
//                            outputStreamForSerial.write(stringToRepresent.getBytes());
                            if ((i + 3) > stringToRepresent.length()) {
                                outputStreamForSerial.write(stringToRepresent.substring(i).getBytes());
                                System.out.println(stringToRepresent.substring(i));
                            } else {
                                outputStreamForSerial.write(stringToRepresent.substring(i, i + 3).getBytes());
                                System.out.println(stringToRepresent.substring(i, i + 3));
                            }
                        }

                        Thread.sleep(3000);


//                            System.out.println(stringToRepresent.substring(i, i + 1));
//                            textToSpeechByArgv(stringToRepresent.substring(i, i + 1));
//                            Thread.sleep((int) delay * 1000);
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        textToSpeechByArgv("Port has shutdown or has been disconnected.");
                    }
//                }

                }
            }
        });
        currentStepsThread.start();


    }


    @FXML
    private void restartStepper(Event e) {
        restartRotation = true;
        try {
            if (!isSerialAlive) {
                startSerialPort();
            }
            outputStreamForSerial.write("aaa".getBytes());
            testValidationLabel.setText("A");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    private void goToStepper(Event e) throws IOException {
        if (!isSerialAlive) {
            startSerialPort();
            System.out.println("inside goToStepper");
        }
        System.out.println(outputStreamForSerial);
        outputStreamForSerial.write(learnGoToField.getText().getBytes());
        if (learnGoToField.isFocused()) {
            anchMain.requestFocus();
        }

    }


    public void startLearnMode(Event e) {
        taskRead = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                rotateToChar();
                return null;
            }
        };
        new Thread(taskRead).start();
    }

    public void uploadTxtToBraille(Event e) {
        if (!isSerialAlive) {
            startSerialPort();
        } else if (isSerialAlive) {
            String Text = writtenTextArea.getText().toLowerCase();
            String i = txtToBrailleDelayField.getText();
            Float delay;
            if (i.equals("")) {
                delay = Float.valueOf(2);
            } else {
                delay = Float.parseFloat(i);
                System.out.println(delay);
            }
            rotateStringOnStepper(Text, delay);
        }
    }

    public void uploadWikipediaToBraille(Event e) {
        if (!isSerialAlive) {
            startSerialPort();
        } else if (isSerialAlive) {
            String Text = wikipediaTextArea.getText().toLowerCase();
            String i = "1";
            Float delay;
            if (i.equals("")) {
                delay = Float.valueOf(2);
            } else {
                delay = Float.parseFloat(i);
                System.out.println(delay);
            }
            rotateStringOnStepper(Text, delay);
        }
    }

    public void uploadNewsToBraille(Event e) {
        if (!isSerialAlive) {
            startSerialPort();
        } else if (isSerialAlive) {
            String Text = articleTextArea.getText().toLowerCase();
            String i = "1";
            Float delay;
            if (i.equals("")) {
                delay = Float.valueOf(2);
            } else {
                delay = Float.parseFloat(i);
                System.out.println(delay);
            }
            rotateStringOnStepper(Text, delay);
        }
    }

    public void uploadDictionaryToBraille(Event e) {
        if (!isSerialAlive) {
            startSerialPort();
        } else if (isSerialAlive) {
            String Text = definitionTextArea.getText().toLowerCase() +
                    synonymsTextArea.getText().toLowerCase() +
                    antonymsTextArea.getText().toLowerCase();
            String i = "2";
            Float delay;
            if (i.equals("")) {
                delay = Float.valueOf(2);
            } else {
                delay = Float.parseFloat(i);
                System.out.println(delay);
            }
            rotateStringOnStepper(Text, delay);
        }
    }


    public void uploadTextToBrailleByEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            uploadTxtToBraille(e);
            txtToBrailleDelayField.clear();
            anchMain.requestFocus();
        }
    }

    private boolean isFocused(int i) {
        if (SCENE_ON_FRONT == i) {
            return true;
        } else {
            return false;
        }
    }

    public void testMode(Event e) {
        Random random = new Random();
        int i = random.nextInt(35);
        String randomChar = str.substring(i, i + 1);
        if (!isSerialAlive) {
            startSerialPort();
        }

        try {
            outputStreamForSerial.write(randomChar.getBytes());
            testValidationLabel.setText(randomChar);

        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    public void uploadPdfToBraille(Event e) {

        if (!isSerialAlive) {
            startSerialPort();
        }

        String content = braillePdfTextArea.getText().toLowerCase();
        String i = pdfToBrailleDelayField.getText().toLowerCase();
        Float delay = Float.valueOf(2); //Default delay - 2 seconds
        if (!i.equals("")) {            //When TextField isn't empty,set the delay to given amount
            delay = Float.parseFloat(i);
        }

        rotateStringOnStepper(content, delay);

    }

    public void uploadPdfToBrailleByEnter(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            uploadPdfToBraille(e);
            pdfToBrailleDelayField.clear();
            anchMain.requestFocus();
        }
    }


    @FXML
    private void appMini(ActionEvent event) {
//        Clipboard clip=Clipboard.getSystemClipboard();
//        System.out.println(clip.getString());
        stage.setIconified(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void writtenTextAreaExit(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            anchMain.requestFocus();
        }
    }


    public void brailleAreaKeyboardPressed(KeyEvent e) {

        if (e.getCode() == KeyCode.F) {
            brailleKeysPressed = brailleKeysPressed + "1";
        } else if (e.getCode() == KeyCode.D) {
            brailleKeysPressed = brailleKeysPressed + "2";
        } else if (e.getCode() == KeyCode.S) {
            brailleKeysPressed = brailleKeysPressed + "3";
        } else if (e.getCode() == KeyCode.J) {
            brailleKeysPressed = brailleKeysPressed + "4";
        } else if (e.getCode() == KeyCode.K) {
            brailleKeysPressed = brailleKeysPressed + "5";
        } else if (e.getCode() == KeyCode.L) {
            brailleKeysPressed = brailleKeysPressed + "6";
        } else if (e.getCode() == KeyCode.SPACE) {
            brailleKeysPressed = "9";
        }


        if (e.getCode() == KeyCode.ENTER) {
            anchMain.requestFocus();
        }
    }

    public void brailleAreaKeyboardReleased(KeyEvent e) {
        String prevStrBraille = brailleTextArea.getText();
        String prevStrText = writtenTextArea.getText();
        System.out.println(brailleKeysPressed);

        String out = converter.brailleKeyboardToChar(brailleKeysPressed);
        char brailleOut = converter.convertChar(out.charAt(0));

        if (!out.equals("n")) {
            writtenTextArea.setText(prevStrText + out);
            brailleTextArea.setText(prevStrBraille + brailleOut);
        } else if (e.getCode() == KeyCode.BACK_SPACE) {
            brailleTextArea.setText(prevStrBraille.substring(0, prevStrBraille.length() - 1));
            writtenTextArea.setText(prevStrText.substring(0, prevStrText.length() - 1));
        }

        brailleKeysPressed = "";
    }
}
