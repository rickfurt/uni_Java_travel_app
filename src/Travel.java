import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.text.DateFormat;
import java.time.*;

public class Travel {
	static int option;
	static String username;
	static double km;
	static int monthOfTravel;
	static int dayOfTravel;
	static double timeOfTravel;
	static boolean peakTime = false;
	static boolean weekend = false;
	static double totalCharge;
	static String dayText = "";
	static int dayNumber = 0;
	static String month = "";
	static String[] companyName = { "Uber", "Didi", "99Taxis" };
	static boolean exit = false;

	// check for input errors
	public static void errorInput(String name, double kms, int month, int day, double time) {
		// if program is not exiting run input data.
		if (!exit) {
			// check user input
			if (name.length() <= 0 || kms <= 0 || month < 1 || month > 12 || day < 0 || day > 31 || time < 0
					|| time > 23.59) {
				System.out.println("\n\nInput errors detected, please check input and try again...");
				userDetails();
			}
		}
	}

	// print star
	public static void star() {
		for (int x = 0; x < 100; x++) {
			System.out.print("*");
		}
	}

	public static void userDetails() {
		boolean isDateValid = false;

		System.out.print("Enter your name \n");
		Scanner in = new Scanner(System.in);
		username = in.next();

		System.out.print("Enter your approximate kilometers of travel \n");
		km = in.nextDouble();

		System.out.print("Month of travel \n");
		monthOfTravel = in.nextInt();

		System.out.print("On which date of this month, you wish to travel \n");
		dayOfTravel = in.nextInt();

		while (!isDateValid) {
			try {
				// Set a local date whose day is found
				LocalDate localDate = LocalDate.of(2021, Month.of(monthOfTravel), dayOfTravel);

				// Find the day from the local date
				DayOfWeek dayOfWeek = DayOfWeek.from(localDate);

				// get month object
				Month x = localDate.getMonth();

				// convert month object to string
				month = x.toString();

				// get day of the week in number 1 to 7
				dayNumber = dayOfWeek.getValue();

				// get day of the week in text.
				dayText = dayOfWeek.name();

				// check if is weekend
				if (dayNumber == 6 || dayNumber == 7) {
					weekend = true;
				} else {
					weekend = false;
				}

				isDateValid = true;

			} catch (Exception e) {
				System.out.println("Date invalid, please check input and try again");

				System.out.print("Month of travel \n");
				monthOfTravel = in.nextInt();

				System.out.print("On which date of the month, you wish to travel \n");
				dayOfTravel = in.nextInt();
				isDateValid = false;
			}
		}
		System.out.print("Now Enter the time of travel - Ps: use 24 hours model example 14.00 \n");
		timeOfTravel = in.nextDouble();

		// check for peaktime
		if (timeOfTravel <= 9 && timeOfTravel >= 7 || timeOfTravel <= 18 && timeOfTravel >= 16) {
			peakTime = true;
		} else {
			peakTime = false;
		}

		menu();
		// invoke method to check for user inputs
		errorInput(username, km, monthOfTravel, dayOfTravel, timeOfTravel);
	}

	public static double calculateCharge(int x, double kms, boolean isPeakTime, boolean isWeekend, int dayNumber,
			String dayText, String monthText, boolean printCharges) {
		// charges organized per index on index 0 is values for company 1 and so on
		double[] baseCharge = { 5.5, 4.5, 4.21 };
		double[] priceForKm = { 0.75, 0.85, 0.68 };
		double[] peakTimeSurcharge = { 2.5, 2, 1.97 };
		double[] weekendSurcharge = { 3, 2.5, 2.98 };

		// calculate the base charge and $/kms
		totalCharge = baseCharge[x] + (kms * priceForKm[x]);

		// check if there is weekend surcharge, and add to total if there is
		if (isWeekend) {
			totalCharge += weekendSurcharge[x];
		}

		// check if there is peak time surcharge, and add to total if there is
		if (isPeakTime) {
			totalCharge += peakTimeSurcharge[x];
		}
		// "if" conditional created in order to calculate and print or only calculate
		// and return value
		if (printCharges) {
			star();
			System.out.println("\nYour travel details:" + "\n\nDay of the week on " + dayNumber + " of " + monthText
					+ " which is " + dayText +
					// ternary conditional for string output
					"\nYour day of travel " + (weekend ? "falls" : "does not falls") + " under weekend category"
					+ (weekend ? " (+ $ " + weekendSurcharge[x] + ")" : "") +
					// ternary conditional for string output
					"\n\n" + (peakTime ? "Time falls " : "Time does not falls ") + "in peaktime category"
					+ (peakTime ? " (+ $ " + peakTimeSurcharge[x] + ")" : "")
					+ "\n\nSo charges will be applied accordingly" + "\n\nThe final charges under company "
					+ companyName[x] + " is: $ " + String.format("%.2f", totalCharge));
			star();
			return 0.0;
		} else {
			return totalCharge;
		}
	}

	// check if the menu option is valid
	public static void isMenuValid(int x) {
		if (x < 1 || x > 6) {
			System.out.print("Invalid menu option");
			menu();
		} else {
			// switch between menus and methods
			switch (x) {
			case 1:
				userDetails();
				break;
			case 2:
				calculateCharge(0, km, peakTime, weekend, dayOfTravel, dayText, month, true);
				break;
			case 3:
				calculateCharge(1, km, peakTime, weekend, dayOfTravel, dayText, month, true);
				break;
			case 4:
				calculateCharge(2, km, peakTime, weekend, dayOfTravel, dayText, month, true);
				break;
			case 5:
				showReport();
				break;
			case 6:
				exit();
				break;
			}
		}
	}

	// show report
	public static void showReport() {
		double[] totalCharges;
		totalCharges = new double[3];
		
		// get the total values based on user details and companies charges
		totalCharges[0] = calculateCharge(0, km, peakTime, weekend, dayOfTravel, dayText, month, false);
		totalCharges[1] = calculateCharge(1, km, peakTime, weekend, dayOfTravel, dayText, month, false);
		totalCharges[2] = calculateCharge(2, km, peakTime, weekend, dayOfTravel, dayText, month, false);

		double mostExpensive = totalCharges[0];
		double cheapest = totalCharges[0];
		String cheapestCompany = "";
		String mostExpensiveCompany = "";

		for (int x = 0; x < totalCharges.length; x++) {
			if (totalCharges[x] > mostExpensive) {
				mostExpensive = totalCharges[x];
				mostExpensiveCompany = companyName[x];
			}
			if (totalCharges[x] < cheapest) {
				cheapest = totalCharges[x];
				cheapestCompany = companyName[x];
			}
		}

		star();
		System.out.println("\n\nThe cheapest company is " + cheapestCompany + ", total charges: $ "
				+ String.format("%.2f", cheapest) + "\n\nThe most expensive company is " + mostExpensiveCompany
				+ ", total charges: $ " + String.format("%.2f", mostExpensive) + "\n");
		star();
		menu();
	}

	public static void menu() {
		System.out.println("\n1. Enter Usage Details" + "\n2. Display Charges under " + companyName[0]
				+ "\n3. Display charges under " + companyName[1] + "\n4. Display charges under " + companyName[2]
				+ "\n5. Show report" + "\n6. Exit ");
		Scanner in = new Scanner(System.in);
		option = in.nextInt();
		isMenuValid(option);
	}

	public static void exit() {
		exit = true;
		System.out.println("Thank You " + username + " for using the system.\n"
				+ "\nAll values have been reset to zero or null \nGood Bye");

		option = 0;
		username = "";
		km = 0;
		monthOfTravel = 0;
		dayOfTravel = 0;
		timeOfTravel = 0;
		totalCharge = 0;
		dayText = "";
		dayNumber = 0;
		month = "";
	}

	public static void main(String[] args) {
		star();
		System.out.println("\nWELCOME TO DIDIBER RIDE-SHARING CHARGES CALCULATOR AND COMPARISON SYSTEM \n");
		System.out.println(
				"Develop by Ricardo Furtado - K200579, Charles Aparecido Silva - K210033, Felipe Bernardes - K201011");
		System.out.println("OODP101 Object Oriented Desing and Programming");

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
		df.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
		final String dateTimeString = df.format(new Date());
		System.out.println(dateTimeString + "\n");
		star();
		menu();

	}
}
