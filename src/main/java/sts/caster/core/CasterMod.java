package sts.caster.core;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.BlockedWordEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnCardUseSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostCreateStartingDeckSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import sts.caster.cards.CasterCardTags;
import sts.caster.cards.attacks.*;
import sts.caster.cards.powers.Focusyn;
import sts.caster.cards.powers.Grimoire;
import sts.caster.cards.powers.Ifrit;
import sts.caster.cards.powers.Incantation;
import sts.caster.cards.powers.Inferno;
import sts.caster.cards.powers.IzanagisObdurance;
import sts.caster.cards.powers.MagicAttunement;
import sts.caster.cards.powers.ManaOverflow;
import sts.caster.cards.powers.MeteorStorm;
import sts.caster.cards.powers.Ramuh;
import sts.caster.cards.powers.ShivasGrace;
import sts.caster.cards.powers.ShortenedChant;
import sts.caster.cards.powers.StaticField;
import sts.caster.cards.powers.SunGoddessAmaterasu;
import sts.caster.cards.skills.Accumulation;
import sts.caster.cards.skills.AshesToAshes;
import sts.caster.cards.skills.BloodRitual;
import sts.caster.cards.skills.CasterDefend;
import sts.caster.cards.skills.Charge;
import sts.caster.cards.skills.Cryogenesis;
import sts.caster.cards.skills.DiablosFlame;
import sts.caster.cards.skills.DivertFocus;
import sts.caster.cards.skills.EchoingVoice;
import sts.caster.cards.skills.Elementalize;
import sts.caster.cards.skills.Embers;
import sts.caster.cards.skills.Enfeeble;
import sts.caster.cards.skills.FireWall;
import sts.caster.cards.skills.FlashFrost;
import sts.caster.cards.skills.Illusion;
import sts.caster.cards.skills.Incinerate;
import sts.caster.cards.skills.MagicBarrier;
import sts.caster.cards.skills.QuickCast;
import sts.caster.cards.skills.Rectify;
import sts.caster.cards.skills.Sultry;
import sts.caster.cards.skills.Surge;
import sts.caster.cards.skills.Unearth;
import sts.caster.cards.skills.WallOfAsh;
import sts.caster.cards.skills.WallOfMirrors;
import sts.caster.cards.skills.WallOfRocks;
import sts.caster.cards.special.Ashes;
import sts.caster.cards.spells.AbsoluteZero;
import sts.caster.cards.spells.AlternatingCurrent;
import sts.caster.cards.spells.Conflagrate;
import sts.caster.cards.spells.DiamondDust;
import sts.caster.cards.spells.Eruption;
import sts.caster.cards.spells.Explosion;
import sts.caster.cards.spells.Fira;
import sts.caster.cards.spells.Fireball;
import sts.caster.cards.spells.Fissure;
import sts.caster.cards.spells.FrostDriver;
import sts.caster.cards.spells.GaiasBlessing;
import sts.caster.cards.spells.GlacialShield;
import sts.caster.cards.spells.Heavy;
import sts.caster.cards.spells.IcicleLance;
import sts.caster.cards.spells.JupitelThunder;
import sts.caster.cards.spells.LightningBolt;
import sts.caster.cards.spells.LordOfVermillion;
import sts.caster.cards.spells.MegaloSpark;
import sts.caster.cards.spells.Meteor;
import sts.caster.cards.spells.NaturalChaos;
import sts.caster.cards.spells.PhoenixFlare;
import sts.caster.cards.spells.PyroclasticTide;
import sts.caster.cards.spells.Sandstorm;
import sts.caster.cards.spells.Sleet;
import sts.caster.cards.spells.Slick;
import sts.caster.cards.spells.SoulStrike;
import sts.caster.cards.spells.SpontaneousCombustion;
import sts.caster.cards.spells.StormGust;
import sts.caster.cards.spells.Susanoo;
import sts.caster.cards.spells.Thundara;
import sts.caster.cards.spells.Tundra;
import sts.caster.cards.spells.WallOfSwords;
import sts.caster.cards.spells.WallOfThorns;
import sts.caster.patches.relics.MagicBookMemorizedCardField;
import sts.caster.potions.PlaceholderPotion;
import sts.caster.relics.DefaultClickableRelic;
import sts.caster.relics.MagicBookRelic;
import sts.caster.relics.PlaceholderRelic;
import sts.caster.util.TextureHelper;
import sts.caster.variables.DelayTurns;
import sts.caster.variables.SecondMagicNumber;
import sts.caster.variables.SpellBlock;
import sts.caster.variables.SpellDamage;


@SpireInitializer
public class CasterMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        OnCardUseSubscriber,
        PostCreateStartingDeckSubscriber,
        OnStartBattleSubscriber,
        PostInitializeSubscriber {

	public static final Logger logger = LogManager.getLogger(CasterMod.class.getName());
    private static String modID;

    private static final String MODNAME = "The Caster";
    private static final String AUTHOR = "Korbo";
    private static final String DESCRIPTION = "The Caster, controller of elements and destroyer of... conical structures.";

    public static HashMap<Integer, Integer> blockLostPerTurn = new HashMap<Integer, Integer>();
    
    // Character Color
    public static final Color CASTER_COLOR = CardHelper.getColor(64.0f, 70.0f, 70.0f);

    // Potion Colors in RGB
    public static final Color PLACEHOLDER_POTION_LIQUID = CardHelper.getColor(209.0f, 53.0f, 18.0f); 
    public static final Color PLACEHOLDER_POTION_HYBRID = CardHelper.getColor(255.0f, 230.0f, 230.0f); 
    public static final Color PLACEHOLDER_POTION_SPOTS = CardHelper.getColor(100.0f, 25.0f, 10.0f);

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

    public static final String ELECTRIFIED_TAG_IMAGE = "caster/images/512/electrified_blank_tag.png";

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

    public static void setModID(String ID) {
        modID = ID;
    }

    public static String getModID() {
        return modID;
    }

    @SuppressWarnings("unused")
    public static void initialize() {
        logger.info("========================= Initializing Default Mod. Hi. =========================");
        CasterMod defaultmod = new CasterMod();
        logger.info("========================= /Default Mod Initialized. Hello World./ =========================");
    }

    @Override
    public void receiveEditCharacters() {
        logger.info("Beginning to edit characters. " + "Add " + TheCaster.Enums.THE_CASTER.toString());

        BaseMod.addCharacter(new TheCaster("The Caster", TheCaster.Enums.THE_CASTER),
                THE_DEFAULT_BUTTON, THE_DEFAULT_PORTRAIT, TheCaster.Enums.THE_CASTER);

        logger.info("Added " + TheCaster.Enums.THE_CASTER.toString());
    }


    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        // Load the Mod Badge
        Texture badgeTexture = TextureHelper.getTexture(BADGE_IMAGE);

        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabel("THis mod doesn't have any settings! Some may come later.", 400.0f, 700.0f,
                settingsPanel, (me) -> {
        }));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        logger.info("Done loading badge Image and mod options");

    }



    public void receiveEditPotions() {
        logger.info("Beginning to edit potions");

        BaseMod.addPotion(PlaceholderPotion.class, PLACEHOLDER_POTION_LIQUID, PLACEHOLDER_POTION_HYBRID, PLACEHOLDER_POTION_SPOTS, PlaceholderPotion.POTION_ID, TheCaster.Enums.THE_CASTER);

        logger.info("Done editing potions");
    }


    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new MagicBookRelic(), TheCaster.Enums.THE_CASTER_COLOR);
        BaseMod.registerBottleRelic(MagicBookMemorizedCardField.inMagicBookField, new MagicBookRelic());

        logger.info("Done adding relics!");
    }


    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        // Add the Custom Dynamic Variables
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new DelayTurns());
        BaseMod.addDynamicVariable(new SpellBlock());
        BaseMod.addDynamicVariable(new SpellDamage());

        logger.info("Adding cards");
        
        BaseMod.addCard(new CasterStrike());
        UnlockTracker.unlockCard(CasterStrike.ID);
        BaseMod.addCard(new CasterDefend());
        UnlockTracker.unlockCard(CasterDefend.ID);
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
        BaseMod.addCard(new Eruption());
        UnlockTracker.unlockCard(Eruption.ID);
        BaseMod.addCard(new LordOfVermillion());
        UnlockTracker.unlockCard(LordOfVermillion.ID);
        BaseMod.addCard(new MeteorStorm());
        UnlockTracker.unlockCard(MeteorStorm.ID);
        BaseMod.addCard(new StormGust());
        UnlockTracker.unlockCard(StormGust.ID);
        BaseMod.addCard(new Accumulation());
        UnlockTracker.unlockCard(Accumulation.ID);
        BaseMod.addCard(new AlternatingCurrent());
        UnlockTracker.unlockCard(AlternatingCurrent.ID);
        BaseMod.addCard(new MagicBarrier());
        UnlockTracker.unlockCard(MagicBarrier.ID);
        BaseMod.addCard(new GateOfBabylon());
        UnlockTracker.unlockCard(GateOfBabylon.ID);
        BaseMod.addCard(new NaturalChaos());
        UnlockTracker.unlockCard(NaturalChaos.ID);
        BaseMod.addCard(new Incinerate());
        UnlockTracker.unlockCard(Incinerate.ID);
        BaseMod.addCard(new IcicleLance());
        UnlockTracker.unlockCard(IcicleLance.ID);
        BaseMod.addCard(new BloodRitual());
        UnlockTracker.unlockCard(BloodRitual.ID);
        BaseMod.addCard(new Grimoire());
        UnlockTracker.unlockCard(Grimoire.ID);
        BaseMod.addCard(new Ifrit());
        UnlockTracker.unlockCard(Ifrit.ID);
        BaseMod.addCard(new GaiasBlessing());
        UnlockTracker.unlockCard(GaiasBlessing.ID);
        BaseMod.addCard(new MegaloSpark());
        UnlockTracker.unlockCard(MegaloSpark.ID);
        BaseMod.addCard(new AbsoluteZero());
        UnlockTracker.unlockCard(AbsoluteZero.ID);
        BaseMod.addCard(new GlacialShield());
        UnlockTracker.unlockCard(GlacialShield.ID);
        BaseMod.addCard(new Ramuh());
        UnlockTracker.unlockCard(Ramuh.ID);
        BaseMod.addCard(new Charge());
        UnlockTracker.unlockCard(Charge.ID);
        BaseMod.addCard(new Sleet());
        UnlockTracker.unlockCard(Sleet.ID);
        BaseMod.addCard(new Unearth());
        UnlockTracker.unlockCard(Unearth.ID);
        BaseMod.addCard(new Sandstorm());
        UnlockTracker.unlockCard(Sandstorm.ID);
        BaseMod.addCard(new Enfeeble());
        UnlockTracker.unlockCard(Enfeeble.ID);
        BaseMod.addCard(new FireWall());
        UnlockTracker.unlockCard(FireWall.ID);
        BaseMod.addCard(new MagicWeapon());
        UnlockTracker.unlockCard(MagicWeapon.ID);
        BaseMod.addCard(new Conflagrate());
        UnlockTracker.unlockCard(Conflagrate.ID);
        BaseMod.addCard(new Susanoo());
        UnlockTracker.unlockCard(Susanoo.ID);
        BaseMod.addCard(new Embers());
        UnlockTracker.unlockCard(Embers.ID);
        BaseMod.addCard(new Rectify());
        UnlockTracker.unlockCard(Rectify.ID);
        BaseMod.addCard(new WallOfSwords());
        UnlockTracker.unlockCard(WallOfSwords.ID);
        BaseMod.addCard(new WallOfAsh());
        UnlockTracker.unlockCard(WallOfAsh.ID);
        BaseMod.addCard(new Inferno());
        UnlockTracker.unlockCard(Inferno.ID);
        BaseMod.addCard(new ShortenedChant());
        UnlockTracker.unlockCard(ShortenedChant.ID);
        BaseMod.addCard(new ShivasGrace());
        UnlockTracker.unlockCard(ShivasGrace.ID);
        BaseMod.addCard(new Focusyn());
        UnlockTracker.unlockCard(Focusyn.ID);
        BaseMod.addCard(new BurnItDown());
        UnlockTracker.unlockCard(BurnItDown.ID);
        BaseMod.addCard(new Barbroot());
        UnlockTracker.unlockCard(Barbroot.ID);
        BaseMod.addCard(new WallOfMirrors());
        UnlockTracker.unlockCard(WallOfMirrors.ID);
        BaseMod.addCard(new Elementalize());
        UnlockTracker.unlockCard(Elementalize.ID);
        BaseMod.addCard(new Sultry());
        UnlockTracker.unlockCard(Sultry.ID);
        BaseMod.addCard(new Cryogenesis());
        UnlockTracker.unlockCard(Cryogenesis.ID);
        BaseMod.addCard(new StaticField());
        UnlockTracker.unlockCard(StaticField.ID);
        BaseMod.addCard(new Discharge());
        UnlockTracker.unlockCard(Discharge.ID);
        BaseMod.addCard(new Surge());
        UnlockTracker.unlockCard(Surge.ID);
        BaseMod.addCard(new AshesToAshes());
        UnlockTracker.unlockCard(AshesToAshes.ID);
        BaseMod.addCard(new DiablosFlame());
        UnlockTracker.unlockCard(DiablosFlame.ID);
        BaseMod.addCard(new Fireball());
        UnlockTracker.unlockCard(Fireball.ID);
        BaseMod.addCard(new Demi());
        UnlockTracker.unlockCard(Demi.ID);
        BaseMod.addCard(new PyroclasticTide());
        UnlockTracker.unlockCard(PyroclasticTide.ID);
        BaseMod.addCard(new FrigidBeam());
        UnlockTracker.unlockCard(FrigidBeam.ID);
        BaseMod.addCard(new Illusion());
        UnlockTracker.unlockCard(Illusion.ID);
        BaseMod.addCard(new IzanagisObdurance());
        UnlockTracker.unlockCard(IzanagisObdurance.ID);
        BaseMod.addCard(new SunGoddessAmaterasu());
        UnlockTracker.unlockCard(SunGoddessAmaterasu.ID);
        BaseMod.addCard(new WallOfRocks());
        UnlockTracker.unlockCard(WallOfRocks.ID);
        BaseMod.addCard(new EchoingVoice());
        UnlockTracker.unlockCard(EchoingVoice.ID);
        BaseMod.addCard(new ManaOverflow());
        UnlockTracker.unlockCard(ManaOverflow.ID);
        BaseMod.addCard(new DiamondDust());
        UnlockTracker.unlockCard(DiamondDust.ID);
        BaseMod.addCard(new Tundra());
        UnlockTracker.unlockCard(Tundra.ID);
        BaseMod.addCard(new SpontaneousCombustion());
        UnlockTracker.unlockCard(SpontaneousCombustion.ID);
        BaseMod.addCard(new Ashes());
        UnlockTracker.unlockCard(Ashes.ID);
        BaseMod.addCard(new PhoenixFlare());
        UnlockTracker.unlockCard(PhoenixFlare.ID);
        BaseMod.addCard(new Fira());
        UnlockTracker.unlockCard(Fira.ID);
        BaseMod.addCard(new Thundara());
        UnlockTracker.unlockCard(Thundara.ID);
        BaseMod.addCard(new Heavy());
        UnlockTracker.unlockCard(Heavy.ID);
        BaseMod.addCard(new Slick());
        UnlockTracker.unlockCard(Slick.ID);
        BaseMod.addCard(new FlashSpeed());
        UnlockTracker.unlockCard(FlashSpeed.ID);
        
        logger.info("Done adding cards!");
    }

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

        //UI Strings
        BaseMod.loadCustomStringsFile(UIStrings.class,
        		getModID() + "/localization/eng/caster-UI-Strings.json");

        logger.info("Done edittting strings");
    }


    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "/localization/eng/caster-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }

    
    public static final int ELECTRIFY_DAMAGE = 2;
	@Override
	public void receiveCardUsed(AbstractCard card) {
		if (card.hasTag(CasterCardTags.ELECTRIFIED)) {
			String electrifiedMessage = CardCrawlGame.languagePack.getUIString("ElectrifiedStrings").TEXT[0];
			AbstractDungeon.effectList.add(new BlockedWordEffect(AbstractDungeon.player, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, electrifiedMessage));
			int tagsBeforeRemove = card.tags.size();
			card.tags.removeIf((tag) -> tag == CasterCardTags.ELECTRIFIED);
			int amountElectrified = tagsBeforeRemove - card.tags.size();
			AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningEffect(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY), 0.1f));
			AbstractDungeon.actionManager.addToBottom(new SFXAction("ORB_LIGHTNING_EVOKE"));
			DamageInfo damage = new DamageInfo(AbstractDungeon.player, ELECTRIFY_DAMAGE * amountElectrified, DamageInfo.DamageType.NORMAL);
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage));
		}
	}

	@Override
	public void receivePostCreateStartingDeck(PlayerClass playerClass, CardGroup deck) {
		if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagicBookRelic.ID)) {
			((MagicBookRelic)AbstractDungeon.player.getRelic(MagicBookRelic.ID)).onTrigger();
		}
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom p0) {
		blockLostPerTurn.clear();
	}
}
