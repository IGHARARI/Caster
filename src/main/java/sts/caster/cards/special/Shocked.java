package sts.caster.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ElectrifySpecificCardAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;

import static sts.caster.core.CasterMod.makeCardPath;

public class Shocked extends CasterCard {

    public static final String ID = CasterMod.makeID("Shocked");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("ashes.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.STATUS;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = -2;

    public Shocked() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.isEthereal = true;
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
    	return false;
    }

    @Override
    public void triggerWhenDrawn() {
        AbstractPlayer p = AbstractDungeon.player;
        for (AbstractCard c : p.hand.group) {
        	addToTop(new ElectrifySpecificCardAction(c));
        }
    }
    
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {}
}
