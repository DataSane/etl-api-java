import client.clientMySQL.ControllerMySQL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        ControllerMySQL controllerMySQL = new ControllerMySQL();

        controllerMySQL.createMunicipios();
        controllerMySQL.insertMunicipios();
        controllerMySQL.selectMunicipios();
    };
}
