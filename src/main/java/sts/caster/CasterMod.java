package sts.caster;

import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import sts.caster.cards.attacks.BookThrow;
import sts.caster.cards.attacks.DefaultRareAttack;
import sts.caster.cards.attacks.LAZER;
import sts.caster.cards.attacks.MagicResonance;
import sts.caster.cards.powers.DefaultCommonPower;
import sts.caster.cards.powers.Incantation;
import sts.caster.cards.powers.MagicAttunement;
import sts.caster.cards.powers.MeteorStorm;
import sts.caster.cards.skills.DivertFocus;
import sts.caster.cards.skills.Explosion;
import sts.caster.cards.skills.Fireball;
import sts.caster.cards.skills.Fissure;
import sts.caster.cards.skills.FlashFrost;
import sts.caster.cards.skills.FrostDriver;
import sts.caster.cards.skills.JupitelThunder;
import sts.caster.cards.skills.LightningBolt;
import sts.caster.cards.skills.LordOfVermillion;
import sts.caster.cards.skills.Meteor;
import sts.caster.cards.skills.Meteor2;
import sts.caster.cards.skills.Meteor3;
import sts.caster.cards.skills.Meteor4;
import sts.caster.cards.skills.Meteor5;
import sts.caster.cards.skills.QuickCast;
import sts.caster.cards.skills.SoulStrike;
import sts.caster.cards.skills.WallOfThorns;
import sts.caster.characters.TheCaster;
import sts.caster.potions.PlaceholderPotion;
import sts.caster.relics.DefaultClickableRelic;
import sts.caster.relics.PlaceholderRelic;
import sts.caster.util.TextureLoader;
import sts.caster.variables.DefaultCustomVariable;
import sts.caster.variables.DelayTurns;


@SpireInitializer
public class CasterMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {

	public static final Logger logger = LogManager.getLogger(CasterMod.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "The Caster";
    private static final String AUTHOR = "Korbo"; // And pretty soon - You!
    private static final String DESCRIPTION = "The Caster, controller of elements and destroyer of... conical structures.";

    // =============== INPUT TEXTURE LOCATION =================

    // Colors (RGB)
    // Character Color
    public static final Color CASTER_COLOR = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); // Orange-ish Red
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); // Near White
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f); // Super Dark Red/Brown

    // Card backgrounds - The actual rectangular card.
    private static final String ATTACK_DEFAULT_GRAY = "caster/images/512/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY = "caster/images/512/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY = "caster/images/512/bg_power_default_gray.png";

    private static final String ENERGY_ORB_DEFAULT_GRAY = "caster/images/512/card_default_gray_orb.png";
    private static final String CARD_ENERGY_ORB = "caster/images/512/card_small_orb.png";

    private static final String ATTACK_DEFAULT_GRAY_PORTRAIT = "caster/images/1024/bg_attack_default_gray.png";
    private static final String SKILL_DEFAULT_GRAY_PORTRAIT = "caster/images/1024/bg_skill_default_gray.png";
    private static final String POWER_DEFAULT_GRAY_PORTRAIT = "caster/images/1024/bg_power_default_gray.png";
    private static final String ENERGY_ORB_DEFAULT_GRAY_PORTRAIT = "caster/images/1024/card_default_gray_orb.png";

    // Character assets
    private static final String THE_DEFAULT_BUTTON = "caster/images/charSelect/DefaultCharacterButton.png";
    private static final String THE_DEFAULT_PORTRAIT = "caster/images/charSelect/DefaultCharacterPortraitBG.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "caster/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "caster/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "caster/images/char/defaultCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "caster/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_DEFAULT_SKELETON_ATLAS = "caster/images/char/defaultCharacter/skeleton.atlas";
    public static final String THE_DEFAULT_SKELETON_JSON = "caster/images/char/defaultCharacter/skeleton.json";

    // =============== MAKE IMAGE PATHS =================

    public static String makeCardPath(String resourcePath) {
        return getModID() + "/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "/images/relics/outline/" + resourcePath;
    }

    public static String makeOrbPath(String resourcePath) {
        return getModID() + "/orbs/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "/images/powers/" + resourcePath;
    }

    public static String makeEventPath(String resourcePath) {
        return getModID() + "/images/events/" + resourcePath;
    }

    // =============== /MAKE IMAGE PATHS/ =================

    // =============== /INPUT TEXTURE LOCATION/ =================


    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================

    public CasterMod() {
        logger.info("Subscribe to BaseMod hooks");

        BaseMod.subscribe(this);
        setModID("caster");

        logger.info("Done subscribing");

        logger.info("Creating the color " + TheCaster.Enums.THE_CASTER_COLOR.toString());

        BaseMod.addColor(TheCaster.Enums.THE_CASTER_COLOR, CASTER_COLOR, CASTER_COLOR, CASTER_COLOR,
                CASTER_COLOR, CASTER_COLOR, CASTER_COLOR, CASTER_COLOR,
                ATTACK_DEFAULT_GRAY, SKILL_DEFAULT_GRAY, POWER_DEFAULT_GRAY, ENERGY_ORB_DEFAULT_GRAY,
                ATTACK_DEFAULT_GRAY_PORTRAIT, SKILL_DEFAULT_GRAY_PORTRAIT, POWER_DEFAULT_GRAY_PORTRAIT,
                ENERGY_ORB_DEFAULT_GRAY_PORTRAIT, CARD_ENERGY_ORB);

        logger.info("Done creating the color");
    }

    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP

    public static void setModID(String ID) {
        if (ID.equals("theDefault")) {
            throw new RuntimeException("Go to your constructor in your class with SpireInitializer and change your mod ID from \"theDefault\"");
        } else if (ID.equals("theDefaultDev")) {
            modID = "theDefault";
        } else {
            modID = ID;
        }
    }

    public static String getModID() {
        return modID;
    }
    // ====== YOU CAN EDIT AGAIN ======


    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        CasterMod defaultmod = new CasterMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    // ============== /SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE/ =================


    // =============== LOAD THE CHARACTER =================

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheCaster.Enums.THE_CASTER.toString());

        BaseMod.addCharacter(new TheCaster("The Caster", TheCaster.Enums.THE_CASTER),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheCaster.Enums.THE_CASTER);

//        receiveEditPotions();
        logger.info("Added " + TheCaster.Enums.THE_CASTER.toString());
    }

    // =============== /LOAD THE CHARACTER/ =================


    // =============== POST-INITIALIZE =================


    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("DefaultMod doesn't have any settings! An example of those may come later.", 400.0f, 700.0f,
                settingsPanel, (me) -> {
        }));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");

    }

    // =============== / POST-INITIALIZE/ =================


    // ================ ADD POTIONS ===================


    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        // Class Specific Potion. If you want your potion to not be class-specific,
        // just remove the player class at the end (in this case the "TheDefaultEnum.THE_DEFAULT".
        // Remember, you can press ctrl+P inside parentheses like addPotions)
        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheCaster.Enums.THE_CASTER);

        logger.info("Done editing potions");
    }

    // ================ /ADD POTIONS/ ===================


    // ================ ADD RELICS ===================

    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new PlaceholderRelic(), TheCaster.Enums.THE_CASTER_COLOR);
        BaseMod.addRelicToCustomPool(new DefaultClickableRelic(), TheCaster.Enums.THE_CASTER_COLOR);

        logger.info("Done adding relics!");
    }

    // ================ /ADD RELICS/ ===================


    // ================ ADD CARDS ===================

    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        // Add the Custom Dynamic Variables
        logger.info("Add variabls");
        // Add the Custom Dynamic variabls
        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DelayTurns());

        logger.info("Adding cards");
        // Add the cards
        BaseMod.addCard(new DefaultCommonPower());
        BaseMod.addCard(new DefaultRareAttack());

        
        BaseMod.addCard(new Meteor());
        UnlockTracker.unlockCard(Meteor.ID);
        BaseMod.addCard(new FlashFrost());
        UnlockTracker.unlockCard(FlashFrost.ID);
        BaseMod.addCard(new LightningBolt());
        UnlockTracker.unlockCard(LightningBolt.ID);
        BaseMod.addCard(new LAZER());
        UnlockTracker.unlockCard(LAZER.ID);
        BaseMod.addCard(new JupitelThunder());
        UnlockTracker.unlockCard(JupitelThunder.ID);
        BaseMod.addCard(new QuickCast());
        UnlockTracker.unlockCard(QuickCast.ID);
        BaseMod.addCard(new WallOfThorns());
        UnlockTracker.unlockCard(WallOfThorns.ID);
        BaseMod.addCard(new MagicResonance());
        UnlockTracker.unlockCard(MagicResonance.ID);
        BaseMod.addCard(new FrostDriver());
        UnlockTracker.unlockCard(FrostDriver.ID);
        BaseMod.addCard(new Fissure());
        UnlockTracker.unlockCard(Fissure.ID);
        BaseMod.addCard(new BookThrow());
        UnlockTracker.unlockCard(BookThrow.ID);
        BaseMod.addCard(new Incantation());
        UnlockTracker.unlockCard(Incantation.ID);
        BaseMod.addCard(new MagicAttunement());
        UnlockTracker.unlockCard(MagicAttunement.ID);
        BaseMod.addCard(new SoulStrike());
        UnlockTracker.unlockCard(SoulStrike.ID);
        BaseMod.addCard(new Explosion());
        UnlockTracker.unlockCard(Explosion.ID);
        BaseMod.addCard(new DivertFocus());
        UnlockTracker.unlockCard(DivertFocus.ID);
        BaseMod.addCard(new Fireball());
        UnlockTracker.unlockCard(Fireball.ID);
        BaseMod.addCard(new LordOfVermillion());
        UnlockTracker.unlockCard(LordOfVermillion.ID);
        BaseMod.addCard(new MeteorStorm());
        UnlockTracker.unlockCard(MeteorStorm.ID);
        
        
        
        BaseMod.addCard(new Meteor2());
        UnlockTracker.unlockCard(Meteor2.ID);
        BaseMod.addCard(new Meteor3());
        UnlockTracker.unlockCard(Meteor3.ID);
        BaseMod.addCard(new Meteor4());
        UnlockTracker.unlockCard(Meteor4.ID);
        BaseMod.addCard(new Meteor5());
        UnlockTracker.unlockCard(Meteor5.ID);
        

        logger.info("Done adding cards!");
    }

    // ================ /ADD CARDS/ ===================


    // ================ LOAD THE TEXT ===================

    @Override
    public void receiveEditStrings() {
        logger.info("Beginning to edit strings");

        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "/localization/eng/caster-Card-Strings.json");

        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "/localization/eng/caster-Power-Strings.json");

        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "/localization/eng/caster-Relic-Strings.json");

        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "/localization/eng/caster-Event-Strings.json");

        // PotionStrings
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                getModID() + "/localization/eng/caster-Potion-Strings.json");

        // CharacterStrings
        BaseMod.loadCustomStringsFile(CharacterStrings.class,
                getModID() + "/localization/eng/caster-Character-Strings.json");

        logger.info("Done edittting strings");
    }

    // ================ /LOAD THE TEXT/ ===================

    // ================ LOAD THE KEYWORDS ===================

    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword

        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "/localization/eng/caster-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    // ================ /LOAD THE KEYWORDS/ ===================    

    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

}
