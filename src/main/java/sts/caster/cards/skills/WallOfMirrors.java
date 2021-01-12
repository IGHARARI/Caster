package sts.caster.cards.skills;

import static sts.caster.core.CasterMod.makeCardPath;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;
import sts.caster.powers.WallOfMirrorsPower;

public class WallOfMirrors extends CasterCard {

    public static final String ID = CasterMod.makeID("WallOfMirrors");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("wallofmirrors.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 2;
    private static final int UPG_COST = 1;


    public WallOfMirrors() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new ApplyPowerAction(m, p, new WallOfMirrorsPower(m, p, 1), 1));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
            upgradeBaseCost(UPG_COST);
        }
    }
}
