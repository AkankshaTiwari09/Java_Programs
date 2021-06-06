
import java.util.*;

public class SalesTax {
	int items;
	String itemList[];
	double totalTax;
	double total;
	//ArrayList<String> food;
	//ArrayList<String> MedicalProducts;
	List<String> food;
	List<String> MedicalProducts;
	
	SalesTax()
	{
		items=0;
		itemList=new String[100];
		totalTax=0.0;
		total=0.0;
		//food=new ArrayList<String>();
		food = Arrays.asList("chocolate","chocolates");
		MedicalProducts= Arrays.asList("headache pill");
		//add_food_items(food);
		//MedicalProducts =new ArrayList<String>();
		//add_medical_products(MedicalProducts);
	}
	/*public void add_food_items(ArrayList<String> food)
	{
		food.add("chocolate bar");
		food.add("chocolates");
	}
	
	public void add_medical_products( ArrayList<String> MedicalProducts)
	{
		MedicalProducts.add("headache pills");
		//MedicalProducts.add("");
	} */

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the amount of items in the list");
		SalesTax obj = new SalesTax();
		obj.items=sc.nextInt();
		sc.nextLine();
		for( int i=0;i<obj.items;i++)
		{
			obj.itemList[i]=sc.nextLine().trim();		
		}
		obj.calculateTax();

	}
	public void calculateTax()
	{
		for( int i=0;i<items;i++)
		{
			double tax_amount=0;
			int len;
			String updatedPrice;
			
			String[] it=itemList[i].split(" ");
			//quantity=Integer.parseInt(it[0]);
			
			len=it.length;
			double quantity=Double.parseDouble(it[0]);
			//Apply taxes based on type of product
			if (!checkProductType(itemList[i]))
			{
				tax_amount+=apply_tax(0.1,"basic",Double.parseDouble(it[len-1])*quantity);
			}
			
			
			if (checkImported(itemList[i]))
			{
				tax_amount+=apply_tax(0.1,"imported",Double.parseDouble(it[len-1])*quantity);
			}
			
			//Update the price by adding the tax amount. Then replace the price of each item 
			//with new price(the price calculated after adding tax)
		updatedPrice=update_price(tax_amount,Double.parseDouble(it[len-1])*quantity);
		itemList[i]=itemList[i].replace(it[len-1], updatedPrice);
		}
		display_result();
	}
	
	public boolean checkImported(String item)
	{
		if(item.indexOf("imported")!=-1)
		{
			return true;
		}
		return false;
	}
	
	public boolean checkProductType(String item)
	{
		if(item.indexOf("book")!=-1)
		{
			return true;
		}
		
		for( String f:food)
		{
			//System.out.println(item+" "+f+" "+item.indexOf(f));
			if(item.indexOf(f)!=-1)
			{
				return true;
			}
		}
		for( String mp: MedicalProducts)
		{
			if(item.indexOf(mp)!=-1)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public double apply_tax(double tax, String type, double price)
	{
		double tax_amount=0.0;
		if(type.equals("basic"))
		{
			tax_amount=price*0.1;
			tax_amount=Math.ceil(tax_amount*20.0)/20.0;    //Round off to nearest 0.05
		}
		else if(type.equals("imported"))
		{
			tax_amount=price*0.05;
			tax_amount=Math.ceil(tax_amount*20.0)/20.0;    //Round off to nearest 0.05
		}
		return tax_amount;
		
	}
	
	public String update_price(double tax_amount, double price)
	{
		//double totalPrice=price*quantity;

		totalTax=totalTax+tax_amount;
		double updatedPrice=price+tax_amount;
		total=total+updatedPrice;
		return String.format("%.2f",updatedPrice);
		
	}
	
	public void display_result()
	{
		for(int i=0;i<items;i++)
		{
			itemList[i]=itemList[i].replace(" at ",": ");
			System.out.println(itemList[i]);
		}
		System.out.println("Sales Taxes: "+String.format("%.2f",totalTax));
		System.out.println("Total: "+String.format("%.2f",total));
		
	}

}
