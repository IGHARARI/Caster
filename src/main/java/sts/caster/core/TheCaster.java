package sts.caster.core;

import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sts.caster.actions.ModifyCardInBattleSpellDamageAction;
import sts.caster.cards.attacks.CasterStrike;
import sts.caster.cards.skills.CasterDefend;
import sts.caster.cards.skills.DivertMana;
import sts.caster.cards.skills.FocusMana;
import sts.caster.cards.skills.TapMana;
import sts.caster.cards.spells.ManaBlast;
import sts.caster.cards.spells.Meteor;
import sts.caster.cards.spells.PhoenixFlare;
import sts.caster.relics.MagicBookRelic;

import java.util.ArrayList;

import static sts.caster.core.CasterMod.*;
import static sts.caster.core.TheCaster.Enums.THE_CASTER_COLOR;

public class TheCaster extends CustomPlayer {
    public static final Logger logger = LogManager.getLogger(CasterMod.class.getName());

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_CASTER;
        @SpireEnum(name = "CASTER_COLOR") // These two HAVE to have the same absolutely identical name.
        public static AbstractCard.CardColor THE_CASTER_COLOR;
        @SpireEnum(name = "CASTER_COLOR")
        public static CardLibrary.LibraryType THE_CASTER_LIBRARY_COLOR;
    }

    public static final int ENERGY_PER_TURN = 3;
    public static final int MAX_HP = 64;
    public static final int STARTING_HP = MAX_HP;
    public static final int STARTING_GOLD = 99;
    public static final int CARD_DRAW = 5;
    public static final int ORB_SLOTS = 0;

    private static final String ID = "TheCaster";
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    private static final String[] NAMES = characterStrings.NAMES;
    private static final String[] TEXT = characterStrings.TEXT;


    public static final String[] orbTextures = {
            "caster/images/char/defaultCharacter/orb/layer1.png",
            "caster/images/char/defaultCharacter/orb/layer2.png",
            "caster/images/char/defaultCharacter/orb/layer3.png",
            "caster/images/char/defaultCharacter/orb/layer4.png",
            "caster/images/char/defaultCharacter/orb/layer5.png",
            "caster/images/char/defaultCharacter/orb/layer6.png",
            "caster/images/char/defaultCharacter/orb/layer1d.png",
            "caster/images/char/defaultCharacter/orb/layer2d.png",
            "caster/images/char/defaultCharacter/orb/layer3d.png",
            "caster/images/char/defaultCharacter/orb/layer4d.png",
            "caster/images/char/defaultCharacter/orb/layer5d.png",};


    public TheCaster(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, "caster/images/char/defaultCharacter/orb/vfx.png", null, null, null);

        initializeClass(null, // required call to load textures and setup energy/loadout.
                // I left these in DefaultMod.java (Ctrl+click them to see where they are, Ctrl+hover to see what they read.)
                THE_DEFAULT_SHOULDER_1, // campfire pose
                THE_DEFAULT_SHOULDER_2, // another campfire pose
                THE_DEFAULT_CORPSE, // dead corpse
                getLoadout(), -20.0F, 0.0F, 220.0F, 350.0F, new EnergyManager(ENERGY_PER_TURN)); // energy manager

        loadAnimation(THE_CASTER_SPRITE_ATLAS, THE_CASTER_SPRITE_JSON, 1.8f);
        AnimationState.TrackEntry e = state.setAnimation(0, "animtion0", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(1.0F);

        dialogX = (drawX + 0.0F * Settings.scale); // set location for text bubbles
        dialogY = (drawY + 140.0F * Settings.scale); // you can just copy these values


    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                STARTING_HP, MAX_HP, ORB_SLOTS, STARTING_GOLD, CARD_DRAW, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(CasterStrike.ID);
        retVal.add(CasterStrike.ID);
        retVal.add(ManaBlast.ID);
        retVal.add(ManaBlast.ID);
//        retVal.add(CasterDefend.ID);
        retVal.add(CasterDefend.ID);
        retVal.add(CasterDefend.ID);

        retVal.add(Meteor.ID);
//        retVal.add(DivertFocus.ID);
        retVal.add(DivertMana.ID);
        retVal.add(FocusMana.ID);
        retVal.add(TapMana.ID);

        return retVal;
    }

    // Starting Relics	
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();

        retVal.add(MagicBookRelic.ID);

        return retVal;
    }

    // character Select screen effect
    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_DAGGER_1", 1.25f); // Sound Effect
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false); // Screen Effect
    }

    // character Select on-button-press sound effect
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_DAGGER_1";
    }

    // Should return how much HP your maximum HP reduces by when starting a run at
    // Ascension 14 or higher. (ironclad loses 5, defect and silent lose 4 hp respectively)
    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    // Should return the card color enum to be associated with your character.
    @Override
    public AbstractCard.CardColor getCardColor() {
        return THE_CASTER_COLOR;
    }

    // Should return a color object to be used to color the trail of moving cards
    @Override
    public Color getCardTrailColor() {
        return sts.caster.core.CasterMod.CASTER_COLOR;
    }

    // Should return a BitmapFont object that you can use to customize how your
    // energy is displayed from within the energy orb.
    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    // Should return class name as it appears in run history screen.
    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    //Which card should be obtainable from the Match and Keep event?
    @Override
    public AbstractCard getStartCardForEvent() {
        return new CasterStrike();
    }

    // The class name as it appears next to your player name in-game
    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    // Should return a new instance of your character, sending name as its name parameter.
    @Override
    public AbstractPlayer newInstance() {
        return new TheCaster(name, chosenClass);
    }

    // Should return a Color object to be used to color the miniature card images in run history.
    @Override
    public Color getCardRenderColor() {
        return sts.caster.core.CasterMod.CASTER_COLOR;
    }

    // Should return a Color object to be used as screen tint effect when your
    // character attacks the heart.
    @Override
    public Color getSlashAttackColor() {
        return sts.caster.core.CasterMod.CASTER_COLOR;
    }

    // Should return an AttackEffect array of any size greater than 0. These effects
    // will be played in sequence as your character's finishing combo on the heart.
    // Attack effects are the same as used in DamageAction and the like.
    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY};
    }

    // Should return a string containing what text is shown when your character is
    // about to attack the heart. For example, the defect is "NL You charge your
    // core to its maximum..."
    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    // The vampire events refer to the base game characters as "brother", "sister",
    // and "broken one" respectively.This method should return a String containing
    // the full text that will be displayed as the first screen of the vampires event.
    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public void applyStartOfTurnCards() {
        AbstractPlayer p = AbstractDungeon.player;

        p.exhaustPile.group.stream()
            .filter(PhoenixFlare.class::isInstance)
            .map(PhoenixFlare.class::cast)
            .forEach(c -> {
                AbstractDungeon.actionManager.addToBottom(new MoveCardsAction(p.hand, p.exhaustPile, (c2) -> c2 == c));
                AbstractDungeon.actionManager.addToBottom(new ModifyCardInBattleSpellDamageAction(c, c.magicNumber));
            });
        super.applyStartOfTurnCards();
    }
}
