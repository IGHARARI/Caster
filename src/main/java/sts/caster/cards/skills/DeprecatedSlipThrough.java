package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class DeprecatedSlipThrough extends CasterCard {

    public static final String ID = CasterMod.makeID("DeprecatedSlipThrough");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("slipthrough.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_BLOCK = 13;
    private static final int UPG_BLOCK = 4;

    public DeprecatedSlipThrough() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseBlock = block = BASE_BLOCK;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new GainBlockAction(p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPG_BLOCK);
            initializeDescription();
        }
    }
}
