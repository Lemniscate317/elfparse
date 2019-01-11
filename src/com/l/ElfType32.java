package com.l;

import java.util.ArrayList;

/**
 * dr => header
 * <p>
 * Elf32_Addr 4 4 无符号程序地址
 * Elf32_Half 2 2 无符号中等整数
 * Elf32_Off 4 4 无符号文件偏移
 * Elf32_SWord 4 4 有符号大整数
 * Elf32_Word 4 4 无符号大整数
 * unsigned char 1 1 无符号小整数
 */
public class ElfType32 {

    public elf32_rel rel;
    public elf32_rela rela;
    public ArrayList<Elf32_Sym> symList = new ArrayList<Elf32_Sym>();
    public elf32_hdr hdr;//elf头部信息
    public ArrayList<elf32_phdr> phdrList = new ArrayList<elf32_phdr>();//可能会有多个程序头
    public ArrayList<elf32_shdr> shdrList = new ArrayList<elf32_shdr>();//可能会有多个段头
    public ArrayList<elf32_strtb> strtbList = new ArrayList<elf32_strtb>();//可能会有多个字符串值

    public ElfType32() {
        rel = new elf32_rel();
        rela = new elf32_rela();
        hdr = new elf32_hdr();
    }

    /**
     * typedef struct elf32_rel {
     * Elf32_Addr	r_offset;
     * Elf32_Word	r_info;
     * } Elf32_Rel;
     */
    public class elf32_rel {
        public byte[] r_offset = new byte[4];
        public byte[] r_info = new byte[4];

        @Override
        public String toString() {
            return "r_offset:" + Utils.bytes2HexString(r_offset) + ";r_info:" + Utils.bytes2HexString(r_info);
        }
    }

    /**
     * typedef struct elf32_rela{
     * Elf32_Addr	r_offset;
     * Elf32_Word	r_info;
     * Elf32_Sword	r_addend;
     * } Elf32_Rela;
     */
    public class elf32_rela {
        public byte[] r_offset = new byte[4];
        public byte[] r_info = new byte[4];
        public byte[] r_addend = new byte[4];

        @Override
        public String toString() {
            return "r_offset:" + Utils.bytes2HexString(r_offset) + ";r_info:" + Utils.bytes2HexString(r_info) + ";r_addend:" + Utils.bytes2HexString(r_info);
        }
    }

    /**
     * typedef struct elf32_sym{
     * Elf32_Word	st_name;
     * Elf32_Addr	st_value;
     * Elf32_Word	st_size;
     * unsigned char	st_info;
     * unsigned char	st_other;
     * Elf32_Half	st_shndx;
     * } Elf32_Sym;
     */
    public static class Elf32_Sym {
        public byte[] st_name = new byte[4];
        public byte[] st_value = new byte[4];
        public byte[] st_size = new byte[4];
        public byte st_info;
        public byte st_other;
        public byte[] st_shndx = new byte[2];

        @Override
        public String toString() {
            return "st_name:" + Utils.bytes2HexString(st_name)
                    + "\nst_value:" + Utils.bytes2HexString(st_value)
                    + "\nst_size:" + Utils.bytes2HexString(st_size)
                    + "\nst_info:" + (st_info / 16)
                    + "\nst_other:" + (((short) st_other) & 0xF)
                    + "\nst_shndx:" + Utils.bytes2HexString(st_shndx);
        }
    }

    public void printSymList() {
        for (int i = 0; i < symList.size(); i++) {
            System.out.println();
            System.out.println("The " + (i + 1) + " Symbol Table:");
            System.out.println(symList.get(i).toString());
        }
    }

    //Bind字段==》st_info
    public static final int STB_LOCAL = 0;
    public static final int STB_GLOBAL = 1;
    public static final int STB_WEAK = 2;
    //Type字段==》st_other
    public static final int STT_NOTYPE = 0;
    public static final int STT_OBJECT = 1;
    public static final int STT_FUNC = 2;
    public static final int STT_SECTION = 3;
    public static final int STT_FILE = 4;
    /**
     * 这里需要注意的是还需要做一次转化
     *  #define ELF_ST_BIND(x)	((x) >> 4)
     #define ELF_ST_TYPE(x)	(((unsigned int) x) & 0xf)
     */

    /**
     * typedef struct elf32_hdr{
     * unsigned char	e_ident[EI_NIDENT];
     * Elf32_Half	e_type;
     * Elf32_Half	e_machine;
     * Elf32_Word	e_version;
     * Elf32_Addr	e_entry;  // Entry point
     * Elf32_Off	e_phoff;
     * Elf32_Off	e_shoff;
     * Elf32_Word	e_flags;
     * Elf32_Half	e_ehsize;
     * Elf32_Half	e_phentsize;
     * Elf32_Half	e_phnum;
     * Elf32_Half	e_shentsize;
     * Elf32_Half	e_shnum;
     * Elf32_Half	e_shstrndx;
     * } Elf32_Ehdr;
     * <p>
     * 其中，e_ident 数组给出了 ELF 的一些标识信息，这个数组中不同下标的含义如表 2
     * 所示：
     * 表 2 e_ident[] 标识索引
     * 名称 取值 目的
     * EI_MAG0 0 文件标识
     * EI_MAG1 1 文件标识
     * EI_MAG2 2 文件标识
     * EI_MAG3 3 文件标识
     * EI_CLASS 4 文件类
     * EI_DATA 5 数据编码
     * EI_VERSION 6 文件版本
     * EI_PAD 7 补齐字节开始处
     * EI_NIDENT 16 e_ident[]大小
     */
    public class elf32_hdr {
        public byte[] e_ident = new byte[16];
        public byte[] e_type = new byte[2];
        public byte[] e_machine = new byte[2];
        public byte[] e_version = new byte[4];
        public byte[] e_entry = new byte[4];
        public byte[] e_phoff = new byte[4];//这个字段是程序头(Program Header)内容在整个文件的偏移值，我们可以用这个偏移值来定位程序头的开始位置，用于解析程序头信息
        public byte[] e_shoff = new byte[4];//这个字段是段头(Section Header)内容在这个文件的偏移值，我们可以用这个偏移值来定位段头的开始位置，用于解析段头信息
        public byte[] e_flags = new byte[4];
        public byte[] e_ehsize = new byte[2];
        public byte[] e_phentsize = new byte[2];
        public byte[] e_phnum = new byte[2];//这个字段是程序头的个数，用于解析程序头信息
        public byte[] e_shentsize = new byte[2];
        public byte[] e_shnum = new byte[2];//这个字段是段头的个数，用于解析段头信息
        public byte[] e_shstrndx = new byte[2];//这个字段是String段在整个段列表中的索引值，这个用于后面定位String段的位置

        @Override
        public String toString() {
            return "magic:" + Utils.bytes2HexString(e_ident)
                    + "\ne_type:" + Utils.bytes2HexString(e_type)
                    + "\ne_machine:" + Utils.bytes2HexString(e_machine)
                    + "\ne_version:" + Utils.bytes2HexString(e_version)
                    + "\ne_entry:" + Utils.bytes2HexString(e_entry)
                    + "\ne_phoff:" + Utils.bytes2HexString(e_phoff)
                    + "\ne_shoff:" + Utils.bytes2HexString(e_shoff)
                    + "\ne_flags:" + Utils.bytes2HexString(e_flags)
                    + "\ne_ehsize:" + Utils.bytes2HexString(e_ehsize)
                    + "\ne_phentsize:" + Utils.bytes2HexString(e_phentsize)
                    + "\ne_phnum:" + Utils.bytes2HexString(e_phnum)
                    + "\ne_shentsize:" + Utils.bytes2HexString(e_shentsize)
                    + "\ne_shnum:" + Utils.bytes2HexString(e_shnum)
                    + "\ne_shstrndx:" + Utils.bytes2HexString(e_shstrndx);
        }
    }

    /**
     * typedef struct elf32_phdr{
     * Elf32_Word	p_type;
     * Elf32_Off	p_offset;
     * Elf32_Addr	p_vaddr;
     * Elf32_Addr	p_paddr;
     * Elf32_Word	p_filesz;
     * Elf32_Word	p_memsz;
     * Elf32_Word	p_flags;
     * Elf32_Word	p_align;
     * } Elf32_Phdr;
     *
     * 名字 取值 说明
     * PT_NULL 0 此数组元素未用。结构中其他成员都是未定义的。
     * PT_LOAD 1
     * 此数组元素给出一个可加载的段，段的大小由 p_filesz 和 p_memsz
     * 描述。文件中的字节被映射到内存段开始处。如果 p_memsz 大于
     * p_filesz，“剩余”的字节要清零。p_filesz 不能大于 p_memsz。可加载
     * 的段在程序头部表格中根据 p_vaddr 成员按升序排列。
     * PT_DYNAMIC 2 数组元素给出动态链接信息。
     * PT_INTERP 3
     * 数组元素给出一个 NULL 结尾的字符串的位置和长度，该字符串将被
     * 当作解释器调用。这种段类型仅对与可执行文件有意义（尽管也可能
     * 在共享目标文件上发生）。在一个文件中不能出现一次以上。如果存在
     * 这种类型的段，它必须在所有可加载段项目的前面。
     * PT_NOTE 4 此数组元素给出附加信息的位置和大小。
     * PT_SHLIB 5 此段类型被保留，不过语义未指定。包含这种类型的段的程序与 ABI
     * 不符。
     * PT_PHDR 6
     * 此类型的数组元素如果存在，则给出了程序头部表自身的大小和位置，
     * 既包括在文件中也包括在内存中的信息。此类型的段在文件中不能出
     * 现一次以上。并且只有程序头部表是程序的内存映像的一部分时才起
     * 作用。如果存在此类型段，则必须在所有可加载段项目的前面。
     * PT_LOPROC 0x70000000
     * PT_HIPROC 0x7fffffff 此范围的类型保留给处理器专用语义。
     */
    public static class elf32_phdr {
        public byte[] p_type = new byte[4];//此数组元素描述的段的类型，或者如何解释此数组元素的信息
        public byte[] p_offset = new byte[4];//此成员给出从文件头到该段第一个字节的偏移。
        public byte[] p_vaddr = new byte[4];//此成员给出段的第一个字节将被放到内存中的虚拟地址。
        public byte[] p_paddr = new byte[4];//此成员仅用于与物理地址相关的系统中。因为 System V 忽略所有应用程序的物理地址信息，此字段对与可执行文件和共享目标文件而言具体内容是未指定的。
        public byte[] p_filesz = new byte[4];//此成员给出段在文件映像中所占的字节数。可以为 0。
        public byte[] p_memsz = new byte[4];//此成员给出段在内存映像中占用的字节数。可以为 0。
        public byte[] p_flags = new byte[4];//此成员给出与段相关的标志。
        /**
         * 可加载的进程段的 p_vaddr 和 p_offset 取值必须合适，相对
         * 于对页面大小的取模而言。此成员给出段在文件中和内存中如何
         * 对齐。数值 0 和 1 表示不需要对齐。否则 p_align 应该是个
         * 正整数，并且是 2 的幂次数，p_vaddr 和 p_offset 对 p_align
         * 取模后应该相等。
         */
        public byte[] p_align = new byte[4];

        @Override
        public String toString() {
            return "p_type:" + Utils.bytes2HexString(p_type)
                    + "\np_offset:" + Utils.bytes2HexString(p_offset)
                    + "\np_vaddr:" + Utils.bytes2HexString(p_vaddr)
                    + "\np_paddr:" + Utils.bytes2HexString(p_paddr)
                    + "\np_filesz:" + Utils.bytes2HexString(p_filesz)
                    + "\np_memsz:" + Utils.bytes2HexString(p_memsz)
                    + "\np_flags:" + Utils.bytes2HexString(p_flags)
                    + "\np_align:" + Utils.bytes2HexString(p_align);
        }
    }

    public void printPhdrList() {
        for (int i = 0; i < phdrList.size(); i++) {
            System.out.println();
            System.out.println("The " + (i + 1) + " Program Header:");
            System.out.println(phdrList.get(i).toString());
        }
    }

    /**
     * typedef struct elf32_shdr {
     * Elf32_Word	sh_name;
     * Elf32_Word	sh_type;
     * Elf32_Word	sh_flags;
     * Elf32_Addr	sh_addr;
     * Elf32_Off	sh_offset;
     * Elf32_Word	sh_size;
     * Elf32_Word	sh_link;
     * Elf32_Word	sh_info;
     * Elf32_Word	sh_addralign;
     * Elf32_Word	sh_entsize;
     * } Elf32_Shdr;
     */
    public static class elf32_shdr {
        public byte[] sh_name = new byte[4];
        public byte[] sh_type = new byte[4];
        public byte[] sh_flags = new byte[4];
        public byte[] sh_addr = new byte[4];
        public byte[] sh_offset = new byte[4];
        public byte[] sh_size = new byte[4];
        public byte[] sh_link = new byte[4];
        public byte[] sh_info = new byte[4];
        public byte[] sh_addralign = new byte[4];
        public byte[] sh_entsize = new byte[4];

        @Override
        public String toString() {
            return "sh_name:" + Utils.bytes2HexString(sh_name)/*Utils.byte2Int(sh_name)*/
                    + "\nsh_type:" + Utils.bytes2HexString(sh_type)
                    + "\nsh_flags:" + Utils.bytes2HexString(sh_flags)
                    + "\nsh_add:" + Utils.bytes2HexString(sh_addr)
                    + "\nsh_offset:" + Utils.bytes2HexString(sh_offset)
                    + "\nsh_size:" + Utils.bytes2HexString(sh_size)
                    + "\nsh_link:" + Utils.bytes2HexString(sh_link)
                    + "\nsh_info:" + Utils.bytes2HexString(sh_info)
                    + "\nsh_addralign:" + Utils.bytes2HexString(sh_addralign)
                    + "\nsh_entsize:" + Utils.bytes2HexString(sh_entsize);
        }
    }

    /****************sh_type********************/
    public static final int SHT_NULL = 0;
    public static final int SHT_PROGBITS = 1;
    public static final int SHT_SYMTAB = 2;
    public static final int SHT_STRTAB = 3;
    public static final int SHT_RELA = 4;
    public static final int SHT_HASH = 5;
    public static final int SHT_DYNAMIC = 6;
    public static final int SHT_NOTE = 7;
    public static final int SHT_NOBITS = 8;
    public static final int SHT_REL = 9;
    public static final int SHT_SHLIB = 10;
    public static final int SHT_DYNSYM = 11;
    public static final int SHT_NUM = 12;
    public static final int SHT_LOPROC = 0x70000000;
    public static final int SHT_HIPROC = 0x7fffffff;
    public static final int SHT_LOUSER = 0x80000000;
    public static final int SHT_HIUSER = 0xffffffff;
    public static final int SHT_MIPS_LIST = 0x70000000;
    public static final int SHT_MIPS_CONFLICT = 0x70000002;
    public static final int SHT_MIPS_GPTAB = 0x70000003;
    public static final int SHT_MIPS_UCODE = 0x70000004;

    /*****************sh_flag***********************/
    public static final int SHF_WRITE = 0x1;
    public static final int SHF_ALLOC = 0x2;
    public static final int SHF_EXECINSTR = 0x4;
    public static final int SHF_MASKPROC = 0xf0000000;
    public static final int SHF_MIPS_GPREL = 0x10000000;

    public void printShdrList() {
        for (int i = 0; i < shdrList.size(); i++) {
            System.out.println();
            System.out.println("The " + (i + 1) + " Section Header:");
            System.out.println(shdrList.get(i));
        }
    }

    /**
     * 这个地方跟elf.h对不上 暂不知道怎么来得
     */
    public static class elf32_strtb {
        public byte[] str_name;
        public int len;

        @Override
        public String toString() {
            return "str_name:" + str_name
                    + "len:" + len;
        }
    }
}

