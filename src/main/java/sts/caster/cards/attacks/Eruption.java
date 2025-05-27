package sts.caster.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.cards.CasterCard;
import sts.caster.cards.special.Charred;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.powers.BlazedPower;

import static sts.caster.core.CasterMod.makeCardPath;

public class Eruption extends CasterCard {

    public static final String ID = CasterMod.makeID("Eruption");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("eruption.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int BASE_DAMAGE = 10;
    private static final int BASE_LAVA = 1;
    private static final int UPG_LAVA = 1;


    public Eruption() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = BASE_DAMAGE;
        magicNumber = baseMagicNumber = BASE_LAVA;
        cardsToPreview = new Charred();
        setCardElement(MagicElement.FIRE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AttackEffect.FIRE));
        addToBot(new ApplyPowerAction(m, p, new BlazedPower(m, p, damage), damage));
        if (!upgraded) {
            addToBot(new MakeTempCardInDrawPileAction(cardsToPreview, magicNumber, true, true));
        } else {
            addToBot(new MakeTempCardInHandAction(cardsToPreview, magicNumber));
        }

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeMagicNumber(UPG_LAVA);
            initializeDescription();
        }
    }
}
