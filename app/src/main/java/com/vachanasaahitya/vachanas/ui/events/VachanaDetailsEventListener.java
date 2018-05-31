package com.vachanasaahitya.vachanas.ui.events;

import com.vachanasaahitya.vachanas.data.Vachana;

public interface VachanaDetailsEventListener {
    void copy(Vachana vachana);
    void share(Vachana vachana);
    void check(Vachana vachana, boolean b);
}
