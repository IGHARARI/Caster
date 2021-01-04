package sts.caster.cards.powers;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FocusPower;

import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.powers.ShortenedChantPower;

public class ShortenedChant extends CasterCard {


    public static final String ID = CasterMod.makeID("ShortenedChant");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("shortchant.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int UPG_COST = 1;
    private static final int BASE_FOCUS_LOSS = 3;
    private static final int UPG_FOCUS_LOSS = -1;
    
    
    public ShortenedChant() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = BASE_FOCUS_LOSS;
    }
    
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ShortenedChantPower(p, 1), 1));
        addToBot(new ApplyPowerAction(p, p, new FocusPower(p, -magicNumber), -magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPG_COST);
            upgradeMagicNumber(UPG_FOCUS_LOSS);
        }
    }
}
