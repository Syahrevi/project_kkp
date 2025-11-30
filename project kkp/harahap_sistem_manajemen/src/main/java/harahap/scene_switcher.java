package harahap;

import javafx.event.ActionEvent;

public class scene_switcher {
    public void switch_to_jadwal_mengajar(ActionEvent event)throws Exception{
        App.setRoot("jadwal_mengajar");
    }
    public void switch_to_crud_pelatih(ActionEvent event)throws Exception{
        App.setRoot("crud_pelatih");
    }
    public void switch_to_crud_siswa(ActionEvent event)throws Exception{
        App.setRoot("crud_siswa");
    }
    public void switch_to_crud_tim_siswa(ActionEvent event)throws Exception{
        App.setRoot("crud_tim_siswa");
    }
    public void switch_to_laporan_gaji(ActionEvent event)throws Exception{
        App.setRoot("laporan_gaji");
    }
    public void switch_to_laporan_absensi(ActionEvent event)throws Exception{
        App.setRoot("laporan_absensi");
    }
}
