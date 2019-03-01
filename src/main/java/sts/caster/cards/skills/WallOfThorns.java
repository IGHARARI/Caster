package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import java.util.ArrayList;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import sts.caster.actions.QueueDelayedCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

public class WallOfThorns extends CasterCard {

    public static final String ID = CasterMod.makeID("WallOfThorns");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("wallofthorns.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int BASE_DELAY = 2;
    private static final int BASE_BLOCK = 5;
    private static final int UPG_BLOCK = 2;
    private static final int BASE_THORNS = 3;


    public WallOfThorns() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_THORNS;
        baseSpellBlock = spellBlock = BASE_BLOCK;
        baseDelayTurns = delayTurns = BASE_DELAY;
        cardElement = MagicElement.EARTH;
        this.tags.add(TheCaster.Enums.DELAYED_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, spellBlock));
    	ArrayList<AbstractGameAction> actions = new ArrayList<AbstractGameAction>();
    	actions.add(new GainBlockAction(p, p, spellBlock));
    	actions.add(new GainBlockAction(p, p, spellBlock));
    	actions.add(new ApplyPowerAction(p, p, new ThornsPower(p, magicNumber), magicNumber));
		AbstractDungeon.actionManager.addToBottom(new QueueDelayedCardAction(this, delayTurns, actions, m));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeSpellBlock(UPG_BLOCK);
        }
    }
}
