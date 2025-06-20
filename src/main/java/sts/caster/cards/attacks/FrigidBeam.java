package sts.caster.cards.attacks;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sts.caster.actions.FreezeRandomCardsAction;
import sts.caster.cards.CasterCard;
import sts.caster.core.CasterMod;
import sts.caster.core.MagicElement;
import sts.caster.core.TheCaster;
import sts.caster.vfx.FrozenLaserEffect;

import static sts.caster.core.CasterMod.makeCardPath;

public class FrigidBeam extends CasterCard {

    public static final String ID = CasterMod.makeID("FrigidBeam");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Attack.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheCaster.Enums.THE_CASTER_COLOR;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int FREEZE_AMT = 1;

    public FrigidBeam() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        damage = baseDamage = DAMAGE;
        magicNumber = baseMagicNumber = FREEZE_AMT;
        cardElement = MagicElement.ICE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    	addToBot(new FreezeRandomCardsAction(magicNumber));
    	addToBot(new VFXAction(p, new FrozenLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.30f));
		addToBot(new DamageAction(m, new DamageInfo(m, damage)));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}
