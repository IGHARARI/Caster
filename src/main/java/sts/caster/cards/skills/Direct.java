package sts.caster.cards.skills;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupMoveAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.cards.CasterCardTags;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;

import static com.megacrit.cardcrawl.cards.CardGroup.CardGroupType.*;
import static sts.caster.core.CasterMod.makeCardPath;

public class Direct extends CasterCard {

    public static final String ID = CasterMod.makeID("Direct");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("direct.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DRAW_AMOUNT = 1;
    private static final int UPG_DRAW_AMOUNT = 1;
    private static final int BLOCK_AMOUNT = 5;
    private static final int UPG_BLOCK_AMOUNT = 2;

    public Direct() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DRAW_AMOUNT;
        baseBlock = block = BLOCK_AMOUNT;
        setCardElement(MagicElement.ELECTRIC);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
//        addToBot(new FetchAction(p.drawPile, c -> BattleHelper.getCardEffectiveCost(c) == 0, magicNumber));
        addToBot(new MultiGroupMoveAction(HAND, magicNumber, true, c -> c.hasTag(CasterCardTags.ELECTRIFIED), DRAW_PILE, DISCARD_PILE));
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DRAW_AMOUNT);
            upgradeBlock(UPG_BLOCK_AMOUNT);
            initializeDescription();
        }
    }
}
