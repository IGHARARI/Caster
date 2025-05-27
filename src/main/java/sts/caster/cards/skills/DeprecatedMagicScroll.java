package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ModifyCastTimeForOneTurnAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.patches.spellCardType.CasterCardType;

import static sts.caster.core.CasterMod.makeCardPath;

public class DeprecatedMagicScroll extends CasterCard {

    public static final String ID = CasterMod.makeID("DeprecatedMagicScroll");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("magicscroll.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DRAW_AMOUNT = 2;
    private static final int UPG_DRAW_AMOUNT = 1;
    private static final int CAST_MODIFY = 1;

    public DeprecatedMagicScroll() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DRAW_AMOUNT;
        baseM2 = m2 = CAST_MODIFY;
        setCardElement(MagicElement.NEUTRAL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : DrawCardAction.drawnCards) {
                    if (c instanceof CasterCard && c.type == CasterCardType.SPELL) {
                        addToBot(new ModifyCastTimeForOneTurnAction((CasterCard) c, -m2));
                    }
                }
                isDone = true;
            }
        }));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPG_DRAW_AMOUNT);
            initializeDescription();
        }
    }
}
