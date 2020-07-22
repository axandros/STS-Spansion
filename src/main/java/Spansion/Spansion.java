package Spansion;

import Spansion.Cards.*;
import Spansion.Powers.DamagedCount;
import Spansion.Relics.*;
import Spansion.util.IDCheckDontTouchPls;
import Spansion.util.TextureLoader;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import basemod.BaseMod;
import basemod.interfaces.*;


import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@SpireInitializer
public class Spansion implements PostExhaustSubscriber,
        PostBattleSubscriber, PostDungeonInitializeSubscriber
        ,EditRelicsSubscriber, PostInitializeSubscriber, EditStringsSubscriber
        ,EditCardsSubscriber, OnPlayerDamagedSubscriber, OnStartBattleSubscriber

{
    public static final Logger logger = LogManager.getLogger(Spansion.class.getName());
    private int count, totalCount;
    private static int damageTakenThisBattle;
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Test Mod";
    private static final String AUTHOR = "Axandros";
    private static final String DESCRIPTION = "A base for Slay the Spire to start your own mod from.";

    // Settings
    public static Properties SpansionDefaultSettings = new Properties();
    public static final String ENABLE_PLACEHOLDER_SETTINGS = "enablePlaceholder";
    public static boolean enablePlaceholder = true;

    private void resetCounts() {
        totalCount = count = 0;
    }

    // === Input Texture Location ===
    public static final String BADGE_IMAGE = "SpansionResources/images/Badge.png";

    // === Image Paths ===

    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath){
        return getModID() + "Resources/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }

    // === Subscribe and Initialize ===

    public Spansion() {
        logger.info("Subscribe to BaseMod hooks");
        BaseMod.subscribe(this);
        setModID("Spansion");
        logger.info("Done subscribing");

        logger.info("Adding mod settings");
        // This loads the mod settings.
        // The actual mod Button is added below in receivePostInitialize()
        SpansionDefaultSettings.setProperty(ENABLE_PLACEHOLDER_SETTINGS, "FALSE"); // This is the default setting. It's actually set...
        try {
            SpireConfig config = new SpireConfig("Spansion", "SpansionConfig", SpansionDefaultSettings); // ...right here
            // the "fileName" parameter is the name of the file MTS will create where it will save our setting.
            config.load(); // Load the setting and set the boolean to equal it
            enablePlaceholder = config.getBool(ENABLE_PLACEHOLDER_SETTINGS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done adding mod settings");

        resetCounts();
    }

    public static void initialize() {
        System.out.println(">>>Initializing Test Mod");
        new Spansion();
        System.out.println(">>>Finished Initializing Test Mod");
    }

    // === Receive Post ===

    @Override
    public void receivePostInitialize(){
        logger.info("Loading Badge image and mod options");
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Load the Mod Menu
        ModPanel settingsPanel = new ModPanel();

        // Create On/Off Button
        ModLabeledToggleButton enableNormalsButton = new ModLabeledToggleButton("This is the text which goes next to the checkbox.",
                350.0f, 700.0f, Settings.CREAM_COLOR, FontHelper.charDescFont, // Position (trial and error it), color, font
                enablePlaceholder, // Boolean it uses
                settingsPanel, // The mod panel in which this button will be in
                (label) -> {}, // thing??????? idk
                (button) -> { // The actual button:

                    enablePlaceholder = button.enabled; // The boolean true/false will be whether the button is enabled or not
                    try {
                        // And based on that boolean, set the settings and save them
                        SpireConfig config = new SpireConfig("defaultMod", "theDefaultConfig", SpansionDefaultSettings);
                        config.setBool(ENABLE_PLACEHOLDER_SETTINGS, enablePlaceholder);
                        config.save();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        settingsPanel.addUIElement(enableNormalsButton); // Add the button to the settings panel. Button is a go.

        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod Options");
    }

    // Add Cards

    @Override
    public void receiveEditCards() {
        logger.info("Spansion: Loading Cards.");
        //BaseMod.addCard(new OldOneTwo());
        //BaseMod.addCard(new FancyFootwork());
        logger.info("Spansion: Loading TestPower.");
        //BaseMod.addCard(new TestPower());
        logger.info("Spansion: Loaded TestPower.");
        // RED
        BaseMod.addCard(new CauterizingBlood());
        BaseMod.addCard(new WrathfulStrike());
        BaseMod.addCard(new VisionsOfPain());
    }

    // === Add Relics ===
    @Override
    public void receiveEditRelics() {
        logger.info("Adding Relics");

        BaseMod.addRelic(new TestRelic(), RelicType.SHARED);// .addRelicToCustomPool(new TestRelic(), AbstractCard.CardColor.RED);
    }



    @Override
    public void receivePostExhaust(AbstractCard c) {
        count++;
        totalCount++;
    }

    @Override
    public void receivePostBattle(AbstractRoom r) {
        System.out.println(count + " cards were exhausted this battle, " +
                totalCount + " cards have been exhausted so far this act.");
        count = 0;
    }

    @Override
    public void receivePostDungeonInitialize() {
        resetCounts();
    }



    // === Do Not Edit ===
    public static void setModID(String ID) {
        Gson coolG = new Gson();
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8));
        InputStream in = Spansion.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        logger.info("You are attempting to set your mod ID as: " + ID);
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) {
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION);
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) {
            modID = EXCEPTION_STRINGS.DEFAULTID;
        } else {
            modID = ID;
        }
        logger.info("Success! ID is " + modID);
    }

    public static String getModID() {
        return modID;
    }

    private static void pathCheck() {
        Gson coolG = new Gson();
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = Spansion.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json");
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class);
        String packageName = Spansion.class.getPackage().getName();
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources");
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) {
            if (!packageName.equals(getModID())) {
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID());
            }
            if (!resourcePathExists.exists()) {
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources");
            }
        }
    }
    // === Resume Editing ===



    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/Spansion-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/Spansion-Power-Strings.json");

        // RelicStrings
        logger.info("Spansion Starting Relic String load.");
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/Spansion-Relic-Strings.json");

        // Event Strings
        //BaseMod.loadCustomStringsFile(EventStrings.class,
        //        getModID() + "Resources/localization/eng/Spansion-Event-Strings.json");

        // PotionStrings
        //BaseMod.loadCustomStringsFile(PotionStrings.class,
        //       getModID() + "Resources/localization/eng/Spansion-Potion-Strings.json");

        // CharacterStrings
        //BaseMod.loadCustomStringsFile(CharacterStrings.class,
        //       getModID() + "Resources/localization/eng/Spansion-Character-Strings.json");

        // OrbStrings
        //BaseMod.loadCustomStringsFile(OrbStrings.class,
        //   getModID() + "Resources/localization/eng/Spansion-Orb-Strings.json");

        logger.info("Done editing strings");
    }

    @Override
    public int receiveOnPlayerDamaged(int i, DamageInfo damageInfo) {
        damageTakenThisBattle += Math.max( 0,AbstractDungeon.player.currentBlock - damageInfo.base);
        return i;
    }

    public static int GetCurrentDamageTaken() {
        return damageTakenThisBattle;
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom abstractRoom) {
        if(AbstractDungeon.player.masterDeck.getCardNames().contains(new WrathfulStrike().ID))
        {
            AbstractPlayer plr = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction( plr, plr,
                    new DamagedCount(plr, plr, 1))
            );
        }
    }
}