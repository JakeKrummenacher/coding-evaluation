package com.aa.act.interview.org;

import java.util.Optional;
import java.util.Random;
public abstract class Organization {

	private Position root;
	
	public Organization() {
		root = createOrganization();
	}
	
	protected abstract Position createOrganization();
	
	/**
	 * hire the given person as an employee in the position that has that title
	 * 
	 * @param person
	 * @param title
	 * @return the newly filled position or empty if no position has that title
	 */


	//Going through what everything means here. This function is used to find the position desired to be filled
	public Optional<Position> getPosition(Position pos, String title) {

		Optional<Position> currentPosition = Optional.of(pos); //using a passed position (the root as defined above), we set the current index.

		//if the highest position happens to equal the desired title, we'll simply return that position.
		if (currentPosition.get().getTitle().equals(title)) {
			return currentPosition;
		} else {

			//looping through the known direct reports allows us to find these positions if they exist
			for (Position p : pos.getDirectReports()) {
				currentPosition = getPosition(p, title);
				if (currentPosition.isPresent()) {
					return currentPosition;
				}
			}
		}
		//if there ended up being no position with the desired title, we return an empty optional.
		return Optional.empty();
	}

	//So as to avoid (to an extent) matching IDs, I went ahead and made a funciton for random 5-digit integers.
	public int getIdentifier(){
		Random ran = new Random();
		return ran.nextInt(10000, 99999);
	}

	public Optional<Position> hire(Name person, String title) {
		Optional<Position> pos = getPosition(root, title);  //using the title argument, we can see if that exists in the org.
		int randIdentifier = getIdentifier(); //get the random identifier integer

		//if the findPosition() function did not return an empty optional...
		if(pos.isPresent()){
			Position position = pos.get();
			if(position.isFilled()){
				System.out.printf("Position with title of %s is filled!", title);
			}else {
				pos.get().setEmployee(Optional.of(new Employee(randIdentifier, person)));
			}
		}

		//if all else fails, we return an empty optional, signifying that the passed arguments are invalid
		return Optional.empty();
	}

	@Override
	public String toString() {
		return printOrganization(root, "");
	}
	
	private String printOrganization(Position pos, String prefix) {
		StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
		for(Position p : pos.getDirectReports()) {
			sb.append(printOrganization(p, prefix + "\t"));
		}
		return sb.toString();
	}
}
