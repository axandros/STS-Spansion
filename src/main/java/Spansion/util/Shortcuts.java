package Spansion.util;

import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Shortcuts {
    public static boolean IntendsToAttack(AbstractMonster m){
        return (m.intent == AbstractMonster.Intent.ATTACK |
                m.intent == AbstractMonster.Intent.ATTACK_BUFF |
                m.intent == AbstractMonster.Intent.ATTACK_DEBUFF |
                m.intent == AbstractMonster.Intent.ATTACK_DEFEND);
    }
}

