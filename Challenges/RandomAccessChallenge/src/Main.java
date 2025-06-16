import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<Integer, Long> employees = new LinkedHashMap<>();
        try(var ra = new RandomAccessFile("employees.dat", "rw")) {
            ra.seek(0);
            int count = ra.readInt();
            System.out.println("# nbr of total employees : " + count);
            for(int i = 0; i < count; i++) {
                employees.put(ra.readInt(), ra.readLong());
            }
            for(Map.Entry<Integer, Long> entry : employees.entrySet()){
                long pointer = entry.getValue();
                ra.seek(pointer);
                boolean isUpdated = readEmployeeRecord(ra, pointer);
                if(isUpdated){
                    ra.seek(pointer);
                    readEmployeeRecord(ra, pointer);
                }
            }

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean readEmployeeRecord(RandomAccessFile ra, long pointer) throws IOException {
        int id = ra.readInt();
        double salary = ra.readDouble();
        String firstName = ra.readUTF();
        String lastName = ra.readUTF();
        System.out.println(
                "Employee: ID=\"%s\" SALARY=\"%.2f\" FIRST_NAME=\"%s\" LAST_NAME=\"%s\""
                        .formatted(id, salary, firstName.trim(), lastName.trim()));
        if(salary == 0.0){
            System.out.println("Updated");
            ra.seek(pointer + Integer.BYTES);
            ra.writeDouble(1010.12);
            return true;
        }
        return false;
    }
}