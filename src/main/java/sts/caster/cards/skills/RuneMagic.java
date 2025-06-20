package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ModifyAllCastingSpellCastTimeAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;

import static sts.caster.core.CasterMod.makeCardPath;

public class RuneMagic extends CasterCard {

    public static final String ID = CasterMod.makeID("RuneMagic");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("channeling.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = CardColor.COLORLESS;

    private static final int COST = 0;
    private static final int BASE_BLOCK = 5;
    private static final int BLOCK_UPGRADE = 3;
    private static final int SPELL_FF_AMOUNT = 1;


    public RuneMagic() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.selfRetain = true;
        this.block = this.baseBlock = BASE_BLOCK;
        magicNumber = baseMagicNumber = SPELL_FF_AMOUNT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
        addToBot(new ModifyAllCastingSpellCastTimeAction(-magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(BLOCK_UPGRADE);
            initializeDescription();
        }
    }
}
