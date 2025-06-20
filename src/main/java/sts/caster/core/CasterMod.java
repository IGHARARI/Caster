package sts.caster.core;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModPanel;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.actions.ModifyCastingSpellCastTimeAction;
import sts.caster.actions.QueueRedrawMiniCardsAction;
import sts.caster.actions.ShowCardVeryBrieflyAction;
import sts.caster.cards.attacks.*;
import sts.caster.cards.powers.*;
import sts.caster.cards.skills.*;
import sts.caster.cards.special.Charred;
import sts.caster.cards.special.Shocked;
import sts.caster.cards.special.Snowed;
import sts.caster.cards.spells.*;
import sts.caster.core.freeze.ElectrifiedHelper;
import sts.caster.core.freeze.IceWallRetainBlockListener;
import sts.caster.delayedCards.CastingSpellCard;
import sts.caster.delayedCards.SpellCardsArea;
import sts.caster.delayedCards.SpellIntentsManager;
import sts.caster.patches.relics.MagicBookMemorizedCardField;
import sts.caster.relics.FuzzyScroll;
import sts.caster.relics.MagicBookRelic;
import sts.caster.util.TextureHelper;
import sts.caster.variables.DelayTurns;
import sts.caster.variables.SecondMagicNumber;
import sts.caster.variables.SpellBlock;
import sts.caster.variables.SpellDamage;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static sts.caster.core.freeze.FreezeHelper.resetFrozenThisCombatCount;


@SpireInitializer
public class CasterMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostCreateStartingDeckSubscriber,
        OnStartBattleSubscriber,
        PostBattleSubscriber,
        PostInitializeSubscriber,
        PostExhaustSubscriber,
        PostPowerApplySubscriber {

    public static final Logger logger = LogManager.getLogger(CasterMod.class.getName());
    private static String modID;

    private static final String MODNAME = "The Caster";
    private static final String AUTHOR = "Korbo";
    private static final String DESCRIPTION = "The Caster. Blob of oblivion.";

    // SPECIAL MECHANIC ATTRIBUTES
    public static HashMap<Integer, Integer> blockLostPerTurn = new HashMap<Integer, Integer>();

    // Character Color
    public static final Color CASTER_COLOR = CardHelper.getColor(64.0f, 70.0f, 70.0f);

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
    private static final String THE_DEFAULT_BUTTON = "caster/images/charSelect/hatbutton.png";
    private static final String THE_DEFAULT_PORTRAIT = "caster/images/charSelect/casterselect.png";
    public static final String THE_DEFAULT_SHOULDER_1 = "caster/images/char/defaultCharacter/shoulder.png";
    public static final String THE_DEFAULT_SHOULDER_2 = "caster/images/char/defaultCharacter/shoulder2.png";
    public static final String THE_DEFAULT_CORPSE = "caster/images/char/defaultCharacter/corpse.png";

    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "caster/images/Badge.png";

    // Atlas and JSON files for the Animations
    public static final String THE_CASTER_SPRITE_ATLAS = "caster/images/char/defaultCharacter/newball.atlas";
    public static final String THE_CASTER_SPRITE_JSON = "caster/images/char/defaultCharacter/newball.json";

    public static String makeCardPath(String resourcePath) {
        return getModID() + "/images/cards/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return getModID() + "/images/relics/" + resourcePath;
    }

    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "/images/relics/outline/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return getModID() + "/images/powers/" + resourcePath;
    }

    public static String makeVFXPath(String vfxPath) {
        return getModID() + "/images/vfx/" + vfxPath;
    }

    public CasterMod() {
        logger.info("Subscribe to BaseMod hooks");

        // Caster listeners
        BaseMod.subscribe(this);
        BaseMod.subscribe(IceWallRetainBlockListener.getInstance());
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
        CasterMod casterMod = new CasterMod();
        logger.info("========================= /Caster Mod Initialized./ =========================");
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
        settingsPanel.addUIElement(new ModLabel("This mod doesn't have any settings! Some may come later.", 400.0f, 700.0f,
                settingsPanel, (me) -> {
        }));
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);
        logger.info("Done loading badge Image and mod options");

    }


    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        // This adds a character specific relic. Only when you play with the mentioned color, will you get this relic.
        BaseMod.addRelicToCustomPool(new MagicBookRelic(), TheCaster.Enums.THE_CASTER_COLOR);
        BaseMod.registerBottleRelic(MagicBookMemorizedCardField.inMagicBookField, new MagicBookRelic());
        BaseMod.addRelicToCustomPool(new FuzzyScroll(), TheCaster.Enums.THE_CASTER_COLOR);

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


        // BASIC CARDS
        BaseMod.addCard(new CasterStrike());
        UnlockTracker.unlockCard(CasterStrike.ID);
        BaseMod.addCard(new CasterDefend());
        UnlockTracker.unlockCard(CasterDefend.ID);
        BaseMod.addCard(new DivertMana());
        UnlockTracker.unlockCard(DivertMana.ID);
        BaseMod.addCard(new Meteor());
        UnlockTracker.unlockCard(Meteor.ID);


        // EVENT SPECIALS
//        BaseMod.addCard(new Fira());
//        UnlockTracker.unlockCard(Fira.ID);
//        BaseMod.addCard(new Thundara());
//        UnlockTracker.unlockCard(Thundara.ID);
//        BaseMod.addCard(new Heavy());
//        UnlockTracker.unlockCard(Heavy.ID);
//        BaseMod.addCard(new Slick());
//        UnlockTracker.unlockCard(Slick.ID);

        // COMMON CARDS
        BaseMod.addCard(new Intensify());
        UnlockTracker.unlockCard(Intensify.ID);
        BaseMod.addCard(new JupitelThunder());
        UnlockTracker.unlockCard(JupitelThunder.ID);
        BaseMod.addCard(new MagicResonance());
        UnlockTracker.unlockCard(MagicResonance.ID);
        BaseMod.addCard(new GlacialShield());
        UnlockTracker.unlockCard(GlacialShield.ID);
        BaseMod.addCard(new DiablosFlame());
        UnlockTracker.unlockCard(DiablosFlame.ID);
        BaseMod.addCard(new Charge());
        UnlockTracker.unlockCard(Charge.ID);
        BaseMod.addCard(new Enfeeble());
        UnlockTracker.unlockCard(Enfeeble.ID);
        BaseMod.addCard(new Shy());
        UnlockTracker.unlockCard(Shy.ID);
        BaseMod.addCard(new Fireball());
        UnlockTracker.unlockCard(Fireball.ID);
        BaseMod.addCard(new VoltTackle());
        UnlockTracker.unlockCard(VoltTackle.ID);
        BaseMod.addCard(new Kindling());
        UnlockTracker.unlockCard(Kindling.ID);
        BaseMod.addCard(new WallOfThorns());
        UnlockTracker.unlockCard(WallOfThorns.ID);
        BaseMod.addCard(new AlternatingCurrent());
        UnlockTracker.unlockCard(AlternatingCurrent.ID);
        BaseMod.addCard(new BurnItDown());
        UnlockTracker.unlockCard(BurnItDown.ID);
        BaseMod.addCard(new PackedIce());
        UnlockTracker.unlockCard(PackedIce.ID);
        BaseMod.addCard(new Direct());
        UnlockTracker.unlockCard(Direct.ID);
        BaseMod.addCard(new FrostDriver());
        UnlockTracker.unlockCard(FrostDriver.ID);
        BaseMod.addCard(new Mirage());
        UnlockTracker.unlockCard(Mirage.ID);
        BaseMod.addCard(new Surge());
        UnlockTracker.unlockCard(Surge.ID);
        BaseMod.addCard(new Tundra());
        UnlockTracker.unlockCard(Tundra.ID);
        BaseMod.addCard(new Lemonade());
        UnlockTracker.unlockCard(Lemonade.ID);


        // UNCOMMON
        BaseMod.addCard(new Eruption());
        UnlockTracker.unlockCard(Eruption.ID);
        BaseMod.addCard(new AbsoluteZero());
        UnlockTracker.unlockCard(AbsoluteZero.ID);
        BaseMod.addCard(new PrimordialFlame());
        UnlockTracker.unlockCard(PrimordialFlame.ID);
        BaseMod.addCard(new Embers());
        UnlockTracker.unlockCard(Embers.ID);
        BaseMod.addCard(new WallOfSwords());
        UnlockTracker.unlockCard(WallOfSwords.ID);
        BaseMod.addCard(new WallOfAsh());
        UnlockTracker.unlockCard(WallOfAsh.ID);
        BaseMod.addCard(new ShivasWrath());
        UnlockTracker.unlockCard(ShivasWrath.ID);
        BaseMod.addCard(new WallOfMirrors());
        UnlockTracker.unlockCard(WallOfMirrors.ID);
        BaseMod.addCard(new Cryogenesis());
        UnlockTracker.unlockCard(Cryogenesis.ID);
        BaseMod.addCard(new StaticField());
        UnlockTracker.unlockCard(StaticField.ID);
        BaseMod.addCard(new Discharge());
        UnlockTracker.unlockCard(Discharge.ID);
        BaseMod.addCard(new PyroclasticTide());
        UnlockTracker.unlockCard(PyroclasticTide.ID);
        BaseMod.addCard(new Illusion());
        UnlockTracker.unlockCard(Illusion.ID);
        BaseMod.addCard(new WallOfRocks());
        UnlockTracker.unlockCard(WallOfRocks.ID);
        BaseMod.addCard(new Permafrost());
        UnlockTracker.unlockCard(Permafrost.ID);
        BaseMod.addCard(new Congeal());
        UnlockTracker.unlockCard(Congeal.ID);
        BaseMod.addCard(new Inferno());
        UnlockTracker.unlockCard(Inferno.ID);
        BaseMod.addCard(new Superconduct());
        UnlockTracker.unlockCard(Superconduct.ID);
        BaseMod.addCard(new Bzzzt());
        UnlockTracker.unlockCard(Bzzzt.ID);
        BaseMod.addCard(new Gain());
        UnlockTracker.unlockCard(Gain.ID);
        BaseMod.addCard(new Electroplating());
        UnlockTracker.unlockCard(Electroplating.ID);
        BaseMod.addCard(new Thermodynamics());
        UnlockTracker.unlockCard(Thermodynamics.ID);
        BaseMod.addCard(new LightningBolt());
        UnlockTracker.unlockCard(LightningBolt.ID);
        BaseMod.addCard(new BurnOut());
        UnlockTracker.unlockCard(BurnOut.ID);
        BaseMod.addCard(new Avalanche());
        UnlockTracker.unlockCard(Avalanche.ID);
        BaseMod.addCard(new SuperCool());
        UnlockTracker.unlockCard(SuperCool.ID);
        BaseMod.addCard(new Jumper());
        UnlockTracker.unlockCard(Jumper.ID);
        BaseMod.addCard(new Igloo());
        UnlockTracker.unlockCard(Igloo.ID);
        BaseMod.addCard(new MatchBox());
        UnlockTracker.unlockCard(MatchBox.ID);
        BaseMod.addCard(new WallOfLightning());
        UnlockTracker.unlockCard(WallOfLightning.ID);
        BaseMod.addCard(new FireWall());
        UnlockTracker.unlockCard(FireWall.ID);
        BaseMod.addCard(new FrostSeed());
        UnlockTracker.unlockCard(FrostSeed.ID);
        BaseMod.addCard(new AetherflameCatalyst());
        UnlockTracker.unlockCard(AetherflameCatalyst.ID);
        BaseMod.addCard(new BurningSpirit());
        UnlockTracker.unlockCard(BurningSpirit.ID);
        BaseMod.addCard(new IzanagisObdurance());
        UnlockTracker.unlockCard(IzanagisObdurance.ID);


        // RARE CARDS
        BaseMod.addCard(new PhoenixFlare());
        UnlockTracker.unlockCard(PhoenixFlare.ID);
        BaseMod.addCard(new LordOfVermillion());
        UnlockTracker.unlockCard(LordOfVermillion.ID);
        BaseMod.addCard(new SunGoddessAmaterasu());
        UnlockTracker.unlockCard(SunGoddessAmaterasu.ID);
        BaseMod.addCard(new EchoingVoice());
        UnlockTracker.unlockCard(EchoingVoice.ID);
        BaseMod.addCard(new Ifrit());
        UnlockTracker.unlockCard(Ifrit.ID);
        BaseMod.addCard(new Explosion());
        UnlockTracker.unlockCard(Explosion.ID);
        BaseMod.addCard(new Susanoo());
        UnlockTracker.unlockCard(Susanoo.ID);
        BaseMod.addCard(new StormGust());
        UnlockTracker.unlockCard(StormGust.ID);
//        BaseMod.addCard(new GateOfBabylon());
//        UnlockTracker.unlockCard(GateOfBabylon.ID);
        BaseMod.addCard(new Demi());
        UnlockTracker.unlockCard(Demi.ID);
        BaseMod.addCard(new Cool());
        UnlockTracker.unlockCard(Cool.ID);
        BaseMod.addCard(new IceWall());
        UnlockTracker.unlockCard(IceWall.ID);
        BaseMod.addCard(new LightningRod());
        UnlockTracker.unlockCard(LightningRod.ID);
        BaseMod.addCard(new IceAge());
        UnlockTracker.unlockCard(IceAge.ID);
        BaseMod.addCard(new TransmuteSoul());
        UnlockTracker.unlockCard(TransmuteSoul.ID);
        BaseMod.addCard(new CurtainCall());
        UnlockTracker.unlockCard(CurtainCall.ID);

        // UNOBTAINABLE
        BaseMod.addCard(new Shocked());
        UnlockTracker.unlockCard(Shocked.ID);
        BaseMod.addCard(new Snowed());
        UnlockTracker.unlockCard(Snowed.ID);
        BaseMod.addCard(new Charred());
        UnlockTracker.unlockCard(Charred.ID);
        BaseMod.addCard(new RuneMagic());
        UnlockTracker.unlockCard(RuneMagic.ID);
//        BaseMod.addCard(new Ashes());
//        UnlockTracker.unlockCard(Ashes.ID);
//        BaseMod.addCard(new SoulStrike());
//        UnlockTracker.unlockCard(SoulStrike.ID);


        // DEPRECATED
//        BaseMod.addCard(new BookThrow());
//        UnlockTracker.unlockCard(BookThrow.ID);
//        BaseMod.addCard(new FrigidBeam());
//        UnlockTracker.unlockCard(FrigidBeam.ID);
//        BaseMod.addCard(new IcicleLance());
//        UnlockTracker.unlockCard(IcicleLance.ID);
//        BaseMod.addCard(new FireWall());
//        UnlockTracker.unlockCard(FireWall.ID);
//        BaseMod.addCard(new Elementalize());
//        UnlockTracker.unlockCard(Elementalize.ID);
//        BaseMod.addCard(new AshesToAshes());
//        UnlockTracker.unlockCard(AshesToAshes.ID);
//        BaseMod.addCard(new Incinerate());
//        UnlockTracker.unlockCard(Incinerate.ID);
//        BaseMod.addCard(new MeteorStorm());
//        UnlockTracker.unlockCard(MeteorStorm.ID);
//        BaseMod.addCard(new Rectify());
//        UnlockTracker.unlockCard(Rectify.ID);
//        BaseMod.addCard(new BloodRitual());
//        UnlockTracker.unlockCard(BloodRitual.ID);
//        BaseMod.addCard(new MegaloSpark());
//        UnlockTracker.unlockCard(MegaloSpark.ID);
//        BaseMod.addCard(new MagicBarrier());
//        UnlockTracker.unlockCard(MagicBarrier.ID);

        // DEPRECATED V2
//        BaseMod.addCard(new QuickCast());
//        UnlockTracker.unlockCard(QuickCast.ID);
//        BaseMod.addCard(new NaturalChaos());
//        UnlockTracker.unlockCard(NaturalChaos.ID);
//        BaseMod.addCard(new MagicWeapon());
//        UnlockTracker.unlockCard(MagicWeapon.ID);
//        BaseMod.addCard(new LAZER());
//        UnlockTracker.unlockCard(LAZER.ID);
//        BaseMod.addCard(new FlashFrost());
//        UnlockTracker.unlockCard(FlashFrost.ID);

//        BaseMod.addCard(new Incantation());
//        UnlockTracker.unlockCard(Incantation.ID);
//        BaseMod.addCard(new MagicAttunement());
//        UnlockTracker.unlockCard(MagicAttunement.ID);
//        BaseMod.addCard(new Accumulation());
//        UnlockTracker.unlockCard(Accumulation.ID);
//        BaseMod.addCard(new GaiasBlessing());
//        UnlockTracker.unlockCard(GaiasBlessing.ID);
//        BaseMod.addCard(new Unearth());
//        UnlockTracker.unlockCard(Unearth.ID);
//        BaseMod.addCard(new Shatter());
//        UnlockTracker.unlockCard(Shatter.ID);

//        BaseMod.addCard(new ManaOverflow());
//        UnlockTracker.unlockCard(ManaOverflow.ID);
//        BaseMod.addCard(new FreezeInHell());
//        UnlockTracker.unlockCard(FreezeInHell.ID);


        // Deprecated v3
//        BaseMod.addCard(new DeprecatedRamuh());
//        UnlockTracker.unlockCard(DeprecatedRamuh.ID);
//        BaseMod.addCard(new DeprecatedShortenedChant());
//        UnlockTracker.unlockCard(DeprecatedShortenedChant.ID);
//        BaseMod.addCard(new DeprecatedFocusyn());
//        UnlockTracker.unlockCard(DeprecatedFocusyn.ID);
//        BaseMod.addCard(new DeprecatedSlipThrough());
//        UnlockTracker.unlockCard(DeprecatedSlipThrough.ID);
//        BaseMod.addCard(new DeprecatedDiamondDust());
//        UnlockTracker.unlockCard(DeprecatedDiamondDust.ID);
//        BaseMod.addCard(new DeprecatedFlashSpeed());
//        UnlockTracker.unlockCard(DeprecatedFlashSpeed.ID);
//        BaseMod.addCard(new DeprecatedSandstorm());
//        UnlockTracker.unlockCard(DeprecatedSandstorm.ID);
//        BaseMod.addCard(new DeprecatedFissure());
//        UnlockTracker.unlockCard(DeprecatedFissure.ID);
//        BaseMod.addCard(new DeprecatedSpontaneousCombustion());
//        UnlockTracker.unlockCard(DeprecatedSpontaneousCombustion.ID);
//        BaseMod.addCard(new DeprecatedHeatRay());
//        UnlockTracker.unlockCard(DeprecatedHeatRay.ID);
//        BaseMod.addCard(new DeprecatedGrimoire());
//        UnlockTracker.unlockCard(DeprecatedGrimoire.ID);
//        BaseMod.addCard(new DeprecatedGigaDrain());
//        UnlockTracker.unlockCard(DeprecatedGigaDrain.ID);
//        BaseMod.addCard(new DeprecatedSleet());
//        UnlockTracker.unlockCard(DeprecatedSleet.ID);
//        BaseMod.addCard(new DeprecatedMagicScroll());
//        UnlockTracker.unlockCard(DeprecatedMagicScroll.ID);
//        BaseMod.addCard(new DivertFocus());
//        UnlockTracker.unlockCard(DivertFocus.ID);
//        BaseMod.addCard(new Channeling());
//        UnlockTracker.unlockCard(Channeling.ID);

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

        //UI Strings
        BaseMod.loadCustomStringsFile(StanceStrings.class,
                getModID() + "/localization/eng/caster-Stance-Strings.json");

        logger.info("Done editing strings");
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


    @Override
    public void receivePostCreateStartingDeck(PlayerClass playerClass, CardGroup deck) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(MagicBookRelic.ID)) {
            ((MagicBookRelic) AbstractDungeon.player.getRelic(MagicBookRelic.ID)).onTrigger();
        }
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom p0) {
//        DeprecatedFrozenPileManager.resetFrozenCount();
        resetFrozenThisCombatCount();
        blockLostPerTurn.clear();
        ElectrifiedHelper.resetElectrifiedThisCombatCount();
    }

    @Override
    public void receivePostBattle(AbstractRoom abstractRoom) {
        ElectrifiedHelper.resetElectrifiedThisCombatCount();
    }

    @Override
    public void receivePostExhaust(AbstractCard abstractCard) {
        boolean redraw = false;
        if (SpellCardsArea.spellCardsBeingCasted != null) {
            for (CastingSpellCard delayCard : SpellCardsArea.spellCardsBeingCasted) {
                if (delayCard.spellCard instanceof FireWall) {
                    AbstractDungeon.actionManager.addToBottom(new ModifyCastingSpellCastTimeAction(delayCard, -delayCard.spellCard.magicNumber));
                    AbstractDungeon.actionManager.addToBottom(new ShowCardVeryBrieflyAction(delayCard.cardMiniCopy, 0.6f, 0.6f, true));
                    redraw = true;
                }
            }
        }
        if (redraw) {
            AbstractDungeon.actionManager.addToBottom(new QueueRedrawMiniCardsAction());
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        if (SpellIntentsManager.spellIntents != null) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    SpellIntentsManager.refreshSpellIntents();
                    isDone = true;
                }
            });
        }
    }
//    @Override
//    public void receivePostEnergyRecharge() {
//        List<AbstractCard> frozenEmbers = DeprecatedFrozenPileManager.frozenPile.group.stream().filter(c -> c instanceof Embers).collect(Collectors.toList());
//        for (AbstractCard ember : frozenEmbers) {
//            ((Embers)ember).onFrozenAtStartOfTurn();
//        }
//    }
}
