package sts.caster.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import sts.caster.actions.ActionOnAllEnemiesAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.powers.CongealPower;

import static sts.caster.core.CasterMod.makeCardPath;

public class Congeal extends CasterCard {

    public static final String ID = CasterMod.makeID("Congeal");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("congeal.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int STR_LOSS_AMOUNT = 1;
    private static final int STR_LOSS_AMOUNT_UPGRADE = 1;
    private static final int BASE_WEAK = 2;

    public Congeal() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_WEAK;
        baseM2 = m2 = STR_LOSS_AMOUNT;
        this.exhaust = true;
        setCardElement(MagicElement.ICE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        addToBot(new ActionOnAllEnemiesAction(
                m -> new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false), magicNumber)
        ));
        addToBot(new ActionOnAllEnemiesAction(
                m -> new ApplyPowerAction(m, p, new CongealPower(m, p, m2), m2)
        ));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeM2(STR_LOSS_AMOUNT_UPGRADE);
            initializeDescription();
        }
    }
}
