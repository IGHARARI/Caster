package sts.caster.cards.skills;

import com.evacipated.cardcrawl.mod.stslib.actions.common.FetchAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.ModifyCastTimeAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.patches.spellCardType.CasterCardType;
import sts.caster.util.BattleHelper;

import static sts.caster.core.CasterMod.makeCardPath;

public class Direct extends CasterCard {

    public static final String ID = CasterMod.makeID("Direct");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DRAW_AMOUNT = 1;
    private static final int UPG_DRAW_AMOUNT = 1;

    public Direct() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = DRAW_AMOUNT;
        setCardElement(MagicElement.ELECTRIC);
    }

     @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
         addToBot(new FetchAction(p.drawPile, c -> BattleHelper.getCardEffectiveCost(c) == 0, magicNumber));
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
