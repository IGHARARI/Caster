package sts.caster.cards.attacks;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.TheCaster;

import static sts.caster.core.CasterMod.makeCardPath;

public class LAZER extends CasterCard {

    public static final String ID = CasterMod.makeID("LAZER");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("lazer.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 2;
    private static final int TIMES_DAMAGE = 4;
    private static final int UPG_DAMAGE = 1;

    public LAZER() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = damage = DAMAGE;
        magicNumber = baseMagicNumber = TIMES_DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	for(int i = 0; i < magicNumber; i++) {
            SmallLaserEffect lazer = new SmallLaserEffect(p.dialogX, p.dialogY, m.drawX, m.drawY + m.hb.height / 2);
            lazer.duration = 0.5f;
    		addToBot(new VFXAction(lazer, 0.05f));
    		addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AttackEffect.SLASH_HORIZONTAL, true));
    	}
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
            initializeDescription();
        }
    }
}
