package com.vachanasaahitya.vachanas.ui.events;

import com.vachanasaahitya.vachanas.data.Vachanakaara;

public interface VachanakaaraItemEventListener {
    void info(Vachanakaara vachanakaara, int cursorPosition);
    void select(Vachanakaara vachanakaara);
}
